package com.vn.DineNow.services.customer.vnpay;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vn.DineNow.entities.Order;
import com.vn.DineNow.enums.OrderStatus;
import com.vn.DineNow.enums.PaymentStatus;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.payment.OrderPaymentIds;
import com.vn.DineNow.payload.request.payment.PaymentRequest;
import com.vn.DineNow.payload.response.payment.PaymentResponse;
import com.vn.DineNow.repositories.OrderRepository;
import com.vn.DineNow.repositories.PaymentRepository;
import com.vn.DineNow.services.customer.payment.PaymentService;
import com.vn.DineNow.services.owner.order.OwnerOrderService;
import com.vn.DineNow.util.VNPayUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class VnPayServiceImp implements VnPayService {

    final OrderRepository orderRepository;
    final PaymentRepository paymentRepository;
    final PaymentService paymentService;
    final OwnerOrderService orderService;

    @Value("${DineNow.vn_pay.tmn-code}") String vnpTmnCode;
    @Value("${DineNow.vn_pay.hash-secret}") String vnpHashSecret;
    @Value("${DineNow.vn_pay.vnp-url}") String vnpUrl;
    @Value("${DineNow.vn_pay.return-url}") String returnUrl;

    @Override
    public String createPaymentUrl(Long orderId, HttpServletRequest httpRequest) throws CustomException {
        Order order = getOrderOrThrow(orderId);

        boolean isPaid = paymentRepository.existsByOrderAndStatus(order, PaymentStatus.SUCCESS);
        if (isPaid) {
            throw new CustomException(StatusCode.INVALID_ACTION, "Order already paid");
        }

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .amount(order.getTotalPrice())
                .orderId(orderId)
                .build();

        var payment = paymentService.createPayment(paymentRequest);

        String orderInfo = "OID" + orderId + "_PID" + payment.getId();
        String ipAddress = httpRequest.getRemoteAddr();
        String amount = String.valueOf(order.getTotalPrice().multiply(BigDecimal.valueOf(100)).longValue());

        SortedMap<String, String> vnpParams = new TreeMap<>();
        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", vnpTmnCode);
        vnpParams.put("vnp_Amount", amount);
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", payment.getId().toString());
        vnpParams.put("vnp_OrderInfo", orderInfo);
        vnpParams.put("vnp_OrderType", "billpayment");
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl", returnUrl);
        vnpParams.put("vnp_IpAddr", ipAddress);
        vnpParams.put("vnp_CreateDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

        String secureHash = VNPayUtils.createSecureHash(vnpHashSecret, vnpParams);

        String queryUrl = vnpParams.entrySet().stream()
                .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        return vnpUrl + "?" + queryUrl + "&vnp_SecureHash=" + secureHash;
    }


    @Override
    public boolean processCallback(Map<String, String> queryParams) throws CustomException {
        String receivedHash = queryParams.remove("vnp_SecureHash");
        queryParams.remove("vnp_SecureHashType");

        SortedMap<String, String> sortedParams = new TreeMap<>(queryParams);
        String calculatedHash = VNPayUtils.createSecureHash(vnpHashSecret, sortedParams);

        if (!calculatedHash.equalsIgnoreCase(receivedHash)) {
            throw new CustomException(StatusCode.UNAUTHORIZED, "Invalid VNPay signature");
        }

        boolean isSuccess = "00".equals(sortedParams.get("vnp_ResponseCode"));

        String orderInfo = sortedParams.get("vnp_OrderInfo");
        long orderId = Long.parseLong(orderInfo.split("_")[0].replace("OID", ""));
        long paymentId = Long.parseLong(orderInfo.split("_")[1].replace("PID", ""));

        var payment = paymentRepository.findById(paymentId).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, "paymentId", Long.toString(paymentId))
        );

        paymentService.updatePaymentStatusFromCallBackPayment(paymentId, isSuccess ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);

        String transactionNo = sortedParams.get("vnp_TransactionNo");
        if (transactionNo != null && !transactionNo.isBlank()) {
            paymentService.updateTransactionId(paymentId, transactionNo);
        }

        orderService.updateOrderStatusFromCallBackPayment(orderId, isSuccess ? OrderStatus.PAID : OrderStatus.FAILED);

        log.info("VNPay callback processed. orderId={}, paymentId={}, success={}, txnNo={}",
                orderId, paymentId, isSuccess, transactionNo);

        return isSuccess;
    }

    private Order getOrderOrThrow(Long orderId) throws CustomException {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "order", orderId.toString()));
    }

}
