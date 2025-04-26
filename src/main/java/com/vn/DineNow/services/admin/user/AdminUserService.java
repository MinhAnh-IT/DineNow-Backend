package com.vn.DineNow.services.admin.user;

import com.vn.DineNow.dtos.UserDTO;

import java.util.List;

public interface AdminUserService {
    List<UserDTO> getAllUsers();
}
