package com.vn.DineNow.services.admin;

import com.vn.DineNow.dtos.UserDTO;

import java.util.List;

public interface IAdminService {
    List<UserDTO> getAllUsers();
}
