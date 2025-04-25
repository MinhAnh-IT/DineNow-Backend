package com.vn.DineNow.controllers;

import com.vn.DineNow.dtos.UserDTO;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.payload.response.APIResponse;
import com.vn.DineNow.services.admin.user.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("api/admin/")
@RestController
@RequiredArgsConstructor
public class AdminController {
    private final AdminUserService adminService;

    @GetMapping("users")
    public APIResponse<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = adminService.getAllUsers();
        return APIResponse.<List<UserDTO>>builder()
                .status(StatusCode.OK.getCode())
                .message("Users retrieved successfully")
                .data(users)
                .build();
    }




}
