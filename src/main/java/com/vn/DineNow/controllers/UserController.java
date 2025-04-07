package com.vn.DineNow.controllers;

import com.vn.DineNow.dtos.UserDTO;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.payload.response.APIResponse;
import com.vn.DineNow.services.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @GetMapping()
    public APIResponse<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return APIResponse.<List<UserDTO>>builder()
                .status(StatusCode.OK.getCode())
                .message("Users retrieved successfully")
                .data(users)
                .build();
    }
    @PostMapping()
    public APIResponse<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return APIResponse.<UserDTO>builder()
                .status(StatusCode.CREATED.getCode())
                .message("User created successfully")
                .data(createdUser)
                .build();
    }


}
