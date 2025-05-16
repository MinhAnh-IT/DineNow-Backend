package com.vn.DineNow.scheduler;

import com.vn.DineNow.entities.Order;
import com.vn.DineNow.enums.OrderStatus;
import com.vn.DineNow.repositories.OrderRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderScheduler {
    OrderRepository orderRepository;

    @Scheduled(fixedRate = 15 * 60 * 1000)
    public void cancelExpiredOrders() {
        log.info("Checking for expired unpaid orders...");
        OffsetDateTime oneHourAgo = OffsetDateTime.now().minusHours(1);
        List<Order> expiredOrders = orderRepository.findExpiredUnpaidOrders(oneHourAgo);
        log.info("update {} expired unpaid orders", expiredOrders.size());
        for (Order order : expiredOrders) {
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
        }
    }
}