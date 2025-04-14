package com.vn.DineNow.aspect;

import com.vn.DineNow.annotation.RequireEnabledUser;
import com.vn.DineNow.entities.User;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.repositories.UserRepository;
import com.vn.DineNow.util.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
public class RequireEnabledUserAspect {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Around("@annotation(requireEnabledUser)")
    public Object checkEnabled(ProceedingJoinPoint joinPoint, RequireEnabledUser requireEnabledUser) throws Throwable {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new CustomException(StatusCode.UNAUTHORIZED);
        }
        HttpServletRequest request = attrs.getRequest();
        String token = jwtService.extractToken(request);

        if (!jwtService.validateToken(token)) {
            throw new CustomException(StatusCode.INVALID_TOKEN);
        }

        Long userId = jwtService.getUserIdFromJWT(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND));

        if (!Boolean.TRUE.equals(user.getEnabled())) {
            throw new CustomException(StatusCode.ACCOUNT_DISABLED);
        }

        return joinPoint.proceed();
    }
}
