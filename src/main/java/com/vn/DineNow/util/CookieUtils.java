package com.vn.DineNow.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public final class CookieUtils {
    private CookieUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static void addRefreshTokenCookie(HttpServletResponse response, String refreshToken, boolean isSecure) {
        // create a cookie for the refresh token
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(60 * 60 * 24 * 30);
        refreshTokenCookie.setSecure(isSecure);

        // add the cookie to the response
        response.addCookie(refreshTokenCookie);
    }
}
