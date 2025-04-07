package com.vn.DineNow.services.user;

import com.vn.DineNow.dtos.UserDTO;

import java.util.List;

public interface IUserService {
    UserDTO createUser(UserDTO userDTO);
    List<UserDTO> getAllUsers();
}
