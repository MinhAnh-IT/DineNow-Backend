package com.vn.DineNow.repositories.dashboard;

import com.vn.DineNow.enums.OrderStatus;
import com.vn.DineNow.payload.response.statistical.dashboard.AdminDashboardResponseDTO;
import com.vn.DineNow.payload.response.statistical.dashboard.OwnerDashboardResponseDTO;

import java.util.Set;

public interface DashboardRepository {
    OwnerDashboardResponseDTO getOwnerDashboard(long ownerId, Set<OrderStatus> statuses, OrderStatus statusCompleted);
    AdminDashboardResponseDTO getAdminDashboard(Set<OrderStatus> statuses, OrderStatus statusCompleted);

}
