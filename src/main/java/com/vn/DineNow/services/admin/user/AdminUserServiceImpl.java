package com.vn.DineNow.services.admin.user;

import com.vn.DineNow.entities.User;
import com.vn.DineNow.enums.Role;
import com.vn.DineNow.enums.SignWith;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.mapper.UserMapper;
import com.vn.DineNow.payload.request.user.UserCreateRequest;
import com.vn.DineNow.payload.response.user.UserDetailsResponse;
import com.vn.DineNow.payload.response.user.UserResponse;
import com.vn.DineNow.repositories.UserRepository;
import com.vn.DineNow.services.common.email.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    PasswordEncoder passwordEncoder;
    EmailService emailService;

    /**
     * Retrieves a list of all users in the system.
     *
     * @return a list of user DTOs
     */
    @Override
    public List<UserResponse> getAllUsers() {
        var users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toResponseDTO)
                .toList();
    }

    /**
     * Retrieves detailed information about a specific user by ID.
     *
     * @param userID the ID of the user
     * @return the user details DTO
     */
    @Override
    public UserDetailsResponse getUserDetails(long userID) throws CustomException{
        var user = userRepository.findById(userID)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "user", String.valueOf(userID)));
        return userMapper.toEntityDetails(user);
    }

    @Override
    public UserResponse CreateOwner(UserCreateRequest request) throws CustomException {
        if (request.getPhone() != null && userRepository.existsByPhone(request.getPhone())) {
            throw new CustomException(StatusCode.EXIST_PHONE, request.getPhone());
        }
        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail(), SignWith.LOCAL)) {
            throw new CustomException(StatusCode.EXIST_EMAIL, request.getEmail());
        }
        User user = userMapper.toEntity(request);
        user.setEnabled(true);
        user.setRole(Role.OWNER);
        user.setIsVerified(true);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        emailService.sendEmailOwnerAccountCreated(user.getEmail(),
                "Tài khoản chủ sở hữu nhà hàng đã được tạo",
                "owner-account-created", user.getEmail(),
                request.getPassword());
        return userMapper.toResponseDTO(user);
    }
}
