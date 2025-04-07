package com.vn.DineNow.services.user;

import com.vn.DineNow.dtos.UserDTO;
import com.vn.DineNow.repositories.UserRepository;
import com.vn.DineNow.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDTO createUser(UserDTO userDTO) {

        return null;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        var users = userRepository.findAll();
        return users.stream().map(userMapper::toDTO).toList();
    }
}
