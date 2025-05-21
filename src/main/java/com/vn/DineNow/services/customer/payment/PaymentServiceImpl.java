package com.vn.DineNow.services.customer.payment;

import com.vn.DineNow.enums.PaymentStatus;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.mapper.PaymentMapper;
import com.vn.DineNow.payload.request.payment.PaymentRequest;
import com.vn.DineNow.payload.response.payment.PaymentResponse;
import com.vn.DineNow.repositories.PaymentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentServiceImpl implements PaymentService{
    PaymentRepository paymentRepository;
    PaymentMapper paymentMapper;

    @Override
    public PaymentResponse createPayment(PaymentRequest request) throws CustomException {
        var savedPayment = paymentRepository.save(paymentMapper.toEntity(request));
        return paymentMapper.toDTO(savedPayment);
    }

    private boolean isTransitionAllowedForCallback(PaymentStatus currentStatus, PaymentStatus newStatus) {
        return switch (currentStatus){
            case PENDING -> newStatus == PaymentStatus.SUCCESS || newStatus == PaymentStatus.FAILED;
            default -> false;
        };
    }

    /**
     * Update the payment status based on the callback from the payment provider.
     *
     * @param paymentId The ID of the payment to update.
     * @param status    The new status to set.
     * @throws CustomException if the payment is not found or if the transition is not allowed.
     */
    @Override
    public void updatePaymentStatusFromCallBackPayment(long paymentId, PaymentStatus status) throws CustomException {
        var payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "payment", String.valueOf(paymentId)));
        if(isTransitionAllowedForCallback(payment.getStatus(), status)){
            payment.setStatus(status);
            paymentRepository.save(payment);
        }
    }

    /**
     * Update the transaction ID for a payment.
     *
     * @param paymentId     The ID of the payment to update.
     * @param transactionId The new transaction ID to set.
     * @throws CustomException if the payment is not found.
     */
    @Override
    public void updateTransactionId(long paymentId, String transactionId) throws CustomException {
        var payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "payment", String.valueOf(paymentId)));
        if(payment.getTransactionId() == null){
            payment.setTransactionId(transactionId);
            paymentRepository.save(payment);
        } else {
            throw new CustomException(StatusCode.INVALID_ACTION, "Transaction ID already exists for this payment");
        }
    }
}
