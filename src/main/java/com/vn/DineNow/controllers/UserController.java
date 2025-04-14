package com.vn.DineNow.controllers;

import com.vn.DineNow.annotation.RequireEnabledUser;
import com.vn.DineNow.dtos.UserDTO;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.user.UserUpdateDTO;
import com.vn.DineNow.payload.response.APIResponse;
import com.vn.DineNow.payload.response.restaurant.FavoriteRestaurantResponseDTO;
import com.vn.DineNow.security.CustomUserDetails;
import com.vn.DineNow.services.favoriteRestaurant.IFavoriteRestaurantService;
import com.vn.DineNow.services.user.IUserService;
import com.vn.DineNow.validation.ValidRestaurantEnabled;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final IUserService userService;
    private final IFavoriteRestaurantService favoriteRestaurantService;

    @GetMapping("/me")
    @RequireEnabledUser
    public ResponseEntity<APIResponse<UserDTO>> getUserDetail(
            @AuthenticationPrincipal CustomUserDetails userDetails)
            throws CustomException {
        UserDTO userDTO = userService.getUserDetail(userDetails.getUser().getId());
        APIResponse<UserDTO> response = APIResponse.<UserDTO>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(userDTO)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me/favorites")
    @RequireEnabledUser
    public ResponseEntity<APIResponse<List<FavoriteRestaurantResponseDTO>>> getFavoriteRestaurantsOfUser(
            @AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException{
        var listFav = favoriteRestaurantService.getListFavoriteRestaurantOfUser(userDetails.getUser().getId());
        APIResponse<List<FavoriteRestaurantResponseDTO>> response = APIResponse.<List<FavoriteRestaurantResponseDTO>>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(listFav)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/me/favorites/{restaurantID}")
    @RequireEnabledUser
    public  ResponseEntity<APIResponse<FavoriteRestaurantResponseDTO>> addFavResForUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable @ValidRestaurantEnabled long restaurantID) throws CustomException{
        var result = favoriteRestaurantService.addFavoriteRestaurantForUser(userDetails.getUser().getId(), restaurantID);
        APIResponse<FavoriteRestaurantResponseDTO> response = APIResponse.<FavoriteRestaurantResponseDTO>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/me/favorites/{restaurantID}")
    public ResponseEntity<APIResponse<Boolean>> removeFavorite(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable @ValidRestaurantEnabled long restaurantID) throws CustomException{
        var result = favoriteRestaurantService.removeFavorite(userDetails.getUser().getId(), restaurantID);
        APIResponse<Boolean> response = APIResponse.<Boolean>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return  ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    public ResponseEntity<APIResponse<UserDTO>> updateUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UserUpdateDTO userUpdateDTO
            ) throws CustomException{
        var result = userService.updateUser(userDetails.getUser().getId(), userUpdateDTO);
        APIResponse<UserDTO> response = APIResponse.<UserDTO>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }
}
