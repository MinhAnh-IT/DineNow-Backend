package com.vn.DineNow.services.admin;

import com.vn.DineNow.dtos.UserDTO;
import com.vn.DineNow.mapper.UserMapper;
import com.vn.DineNow.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AdminService implements IAdminService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDTO> getAllUsers() {
        var users = userRepository.findAll();
        return users.stream().map(userMapper::toDTO).toList();
    }
}
