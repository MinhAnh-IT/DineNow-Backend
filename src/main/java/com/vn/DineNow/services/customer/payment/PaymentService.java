package com.vn.DineNow.services.customer.payment;

import com.vn.DineNow.enums.PaymentStatus;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.payment.PaymentRequest;
import com.vn.DineNow.payload.response.payment.PaymentResponse;

public interface PaymentService {
    PaymentResponse createPayment(PaymentRequest request) throws CustomException;
    void updatePaymentStatusFromCallBackPayment(long paymentId, PaymentStatus status) throws CustomException;
    void updateTransactionId(long paymentId, String transactionId) throws CustomException;
}
