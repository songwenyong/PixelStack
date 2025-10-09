package com.pixelstack.ims.config;

import com.pixelstack.ims.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");

        if (StringUtils.isBlank(token) || !token.startsWith("Bearer ")) {
            response.setStatus(401);
            response.getWriter().write("{\"code\":401,\"message\":\"未授权访问\"}");
            return false;
        }

        token = token.substring(7);

        try {
            Long userId = jwtUtil.extractUserId(token);
            String username = jwtUtil.extractUsername(token);

            if (jwtUtil.validateToken(token, username)) {
                request.setAttribute("userId", userId);
                request.setAttribute("username", username);
                return true;
            } else {
                response.setStatus(401);
                response.getWriter().write("{\"code\":401,\"message\":\"Token已过期\"}");
                return false;
            }
        } catch (Exception e) {
            log.error("JWT验证失败", e);
            response.setStatus(401);
            response.getWriter().write("{\"code\":401,\"message\":\"Token无效\"}");
            return false;
        }
    }
}