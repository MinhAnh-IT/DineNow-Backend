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


@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;


    @Override
    public UserDTO getUserDetail(long userID) throws CustomException {
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND));
        return userMapper.toDTO(user);
    }

    @Override
    public UserDTO updateUser(long id, UserUpdateDTO userDTO) throws CustomException {
        User user = userRepository.findById(id).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, "user", String.valueOf(id))
        );
        userMapper.updateUserFromDTO(userDTO, user);
        userRepository.save(user);
        return userMapper.toDTO(user);
    }

}
