package com.vn.DineNow.services.customer.user;


import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.user.UserUpdateRequest;
import com.vn.DineNow.payload.response.user.UserResponse;


public interface UserService {
    UserResponse getUserDetail(long userID) throws CustomException;
    UserResponse updateUser(long id, UserUpdateRequest userDTO) throws  CustomException;
}
