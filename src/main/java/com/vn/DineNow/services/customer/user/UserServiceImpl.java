package com.vn.DineNow.services.customer.user;

import com.vn.DineNow.dtos.UserDTO;
import com.vn.DineNow.entities.User;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.user.UserUpdateDTO;
import com.vn.DineNow.repositories.UserRepository;
import com.vn.DineNow.mapper.UserMapper;
import com.vn.DineNow.util.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service implementation for customer user operations like profile retrieval and update.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    /**
     * Retrieves the full user profile by user ID.
     *
     * @param userID the ID of the user
     * @return the user DTO
     * @throws CustomException if the user is not found
     */
    @Override
    public UserDTO getUserDetail(long userID) throws CustomException {
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND));
        return userMapper.toDTO(user);
    }

    /**
     * Updates the user's profile information.
     *
     * @param id      the ID of the user to update
     * @param userDTO the DTO containing updated user info
     * @return the updated user DTO
     * @throws CustomException if the user does not exist
     */
    @Override
    public UserDTO updateUser(long id, UserUpdateDTO userDTO) throws CustomException {
        // Retrieve user by ID or throw not found exception
        User user = userRepository.findById(id).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, "user", String.valueOf(id))
        );

        // Update entity fields using mapper
        userMapper.updateUserFromDTO(userDTO, user);

        // Persist the updated user entity
        userRepository.save(user);

        return userMapper.toDTO(user);
    }
}
