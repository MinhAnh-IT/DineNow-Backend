package com.vn.DineNow.services.admin.user;

import com.vn.DineNow.dtos.UserDTO;
import com.vn.DineNow.mapper.UserMapper;
import com.vn.DineNow.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for managing user data from the admin side.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminUserServiceImpl implements AdminUserService {
    UserRepository userRepository;
    UserMapper userMapper;

    /**
     * Retrieves a list of all users in the system.
     *
     * @return a list of user DTOs
     */
    @Override
    public List<UserDTO> getAllUsers() {
        var users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toDTO)
                .toList();
    }
}
