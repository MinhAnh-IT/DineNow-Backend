package com.vn.DineNow.services.admin.user;

import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.user.UserCreateRequest;
import com.vn.DineNow.payload.response.user.UserDetailsResponse;
import com.vn.DineNow.payload.response.user.UserResponse;

import java.util.List;

public interface AdminUserService {
    List<UserResponse> getAllUsers();
    UserDetailsResponse getUserDetails(long userID) throws CustomException;
    UserResponse CreateOwner(UserCreateRequest request) throws CustomException;
}
