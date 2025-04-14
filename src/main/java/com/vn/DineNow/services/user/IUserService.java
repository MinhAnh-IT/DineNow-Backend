package com.vn.DineNow.services.user;


import com.vn.DineNow.dtos.UserDTO;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.user.UserUpdateDTO;


public interface IUserService {
    UserDTO getUserDetail(long userID) throws CustomException;
    UserDTO updateUser(long id, UserUpdateDTO userDTO) throws  CustomException;
}
