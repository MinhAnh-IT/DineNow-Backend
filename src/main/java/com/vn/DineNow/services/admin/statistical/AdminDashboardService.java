package com.vn.DineNow.services.admin.statistical;

import com.vn.DineNow.payload.response.statistical.dashboard.AdminDashboardResponseDTO;

public interface AdminDashboardService {
    AdminDashboardResponseDTO getDashboardData() throws Exception;
}
