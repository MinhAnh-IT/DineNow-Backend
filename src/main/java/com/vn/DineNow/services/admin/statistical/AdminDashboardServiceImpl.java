package com.vn.DineNow.services.admin.statistical;

import com.vn.DineNow.constrants.OrderStatusConstants;
import com.vn.DineNow.enums.OrderStatus;
import com.vn.DineNow.payload.response.statistical.dashboard.AdminDashboardResponseDTO;
import com.vn.DineNow.repositories.dashboard.DashboardRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminDashboardServiceImpl implements  AdminDashboardService {
    DashboardRepository dashboardRepository;

    public AdminDashboardResponseDTO getDashboardData() throws Exception {
        return dashboardRepository.getAdminDashboard(OrderStatusConstants.SUCCESSFUL_STATUSES, OrderStatus.COMPLETED);
    }
}
