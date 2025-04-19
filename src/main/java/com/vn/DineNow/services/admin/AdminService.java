package com.vn.DineNow.services.admin;

import com.vn.DineNow.dtos.UserDTO;
import com.vn.DineNow.mapper.UserMapper;
import com.vn.DineNow.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminService implements IAdminService {
    UserRepository userRepository;
    UserMapper userMapper;

    @Override
    public List<UserDTO> getAllUsers() {
        var users = userRepository.findAll();
        return users.stream().map(userMapper::toDTO).toList();
    }
}
