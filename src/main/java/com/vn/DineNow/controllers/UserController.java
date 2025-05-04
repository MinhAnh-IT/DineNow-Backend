package com.vn.DineNow.controllers;

import com.vn.DineNow.annotation.RequireEnabledUser;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.user.UserUpdateRequest;
import com.vn.DineNow.payload.response.APIResponse;
import com.vn.DineNow.payload.response.restaurant.FavoriteRestaurantResponseDTO;
import com.vn.DineNow.payload.response.user.UserResponse;
import com.vn.DineNow.security.CustomUserDetails;
import com.vn.DineNow.services.customer.favoriteRestaurant.FavoriteRestaurantService;
import com.vn.DineNow.services.customer.user.UserService;
import com.vn.DineNow.validation.ValidRestaurantApprovedValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/users/")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;
    private final FavoriteRestaurantService favoriteRestaurantService;

    @GetMapping("me")
    @RequireEnabledUser
    public ResponseEntity<APIResponse<UserResponse>> getUserDetail(
            @AuthenticationPrincipal CustomUserDetails userDetails)
            throws CustomException {
        var userDTO = userService.getUserDetail(userDetails.getUser().getId());
        APIResponse<UserResponse> response = APIResponse.<UserResponse>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(userDTO)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("me/favorites")
    @RequireEnabledUser
    public ResponseEntity<APIResponse<List<FavoriteRestaurantResponseDTO>>> getFavoriteRestaurantsOfUser(
            @AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException{
        var listFav = favoriteRestaurantService.getListFavoriteRestaurantOfUser(
                userDetails.getUser().getId());
        APIResponse<List<FavoriteRestaurantResponseDTO>> response = APIResponse.
                <List<FavoriteRestaurantResponseDTO>>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(listFav)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("me/favorites/{restaurantID}")
    @RequireEnabledUser
    public  ResponseEntity<APIResponse<FavoriteRestaurantResponseDTO>> addFavResForUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable @ValidRestaurantApprovedValidator long restaurantID) throws CustomException{
        var result = favoriteRestaurantService.addFavoriteRestaurantForUser(
                userDetails.getUser().getId(), restaurantID);
        APIResponse<FavoriteRestaurantResponseDTO> response = APIResponse.
                <FavoriteRestaurantResponseDTO>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("me/favorites/{restaurantID}")
    public ResponseEntity<APIResponse<Boolean>> removeFavorite(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable @ValidRestaurantApprovedValidator long restaurantID) throws CustomException{
        var result = favoriteRestaurantService.removeFavorite(
                userDetails.getUser().getId(), restaurantID);
        APIResponse<Boolean> response = APIResponse.<Boolean>builder()
                .status(StatusCode.DELETED.getCode())
                .message(StatusCode.DELETED.getMessage())
                .data(result)
                .build();
        return  ResponseEntity.ok(response);
    }

    @PutMapping("me")
    @RequireEnabledUser
    public ResponseEntity<APIResponse<UserResponse>> updateUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid UserUpdateRequest userUpdateDTO
            ) throws CustomException{
        var result = userService.updateUser(
                userDetails.getUser().getId(), userUpdateDTO);
        APIResponse<UserResponse> response = APIResponse.<UserResponse>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }
}
