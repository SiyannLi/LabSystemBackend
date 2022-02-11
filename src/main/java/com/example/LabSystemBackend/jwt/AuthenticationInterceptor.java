package com.example.LabSystemBackend.jwt;


import com.example.LabSystemBackend.controller.UserController;
import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.entity.UserAccountStatus;
import com.example.LabSystemBackend.entity.UserRole;
import com.example.LabSystemBackend.jwt.annotation.AdminLoginToken;
import com.example.LabSystemBackend.jwt.annotation.PassToken;
import com.example.LabSystemBackend.jwt.annotation.SuperAdminLoginToken;
import com.example.LabSystemBackend.jwt.annotation.UserLoginToken;
import com.example.LabSystemBackend.service.UserService;
import com.example.LabSystemBackend.ui.ExceptionMessage;
import com.example.LabSystemBackend.ui.InputMessage;
import com.example.LabSystemBackend.ui.KeyMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;
/**
 * @version 1.0
 * @author Cong Liu
 *
 * Authentication interceptor
 */
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        if (HttpMethod.OPTIONS.toString().equals(httpServletRequest.getMethod())) {
            return true;
        }
        String token = httpServletRequest.getHeader(KeyMessage.TOKEN);
        if (token == null) {
            throw new RuntimeException(ExceptionMessage.TOKEN_NULL);
        }
        Date exp = JwtUtil.getExpiresTime(token);
        Date ref = JwtUtil.getRefreshTime(token);
        String email = JwtUtil.getUserInfo(token, KeyMessage.EMAIL);
        if (!JwtUtil.verify(token) || exp == null || ref == null ||  email == null) {
            UserController.emailTokens.remove(email);
            throw new RuntimeException(ExceptionMessage.TOKEN_WRONG);
        }
        String tokenServer = UserController.emailTokens.get(email);
        if(!token.equals(tokenServer)) {
            UserController.emailTokens.remove(email);
            throw new RuntimeException(ExceptionMessage.TOKEN_CONFLICT);

        }

        if (!userService.emailExists(email)) {
            UserController.emailTokens.remove(email);
            throw new RuntimeException(ExceptionMessage.USER_NOT_EXISTS);
        }
        User user = userService.getUserByEmail(email);
        Date now = new Date();
        if (now.after(exp)) {
            UserController.emailTokens.remove(email);
            throw new RuntimeException(ExceptionMessage.EXPIRED_TOKEN);
        }
        if(now.after(ref) && now.before(exp)) {
            String newToken = JwtUtil.createToken(user);
            UserController.emailTokens.put(email, newToken);
        }

        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                if (!UserAccountStatus.ACTIVE.equals(user.getUserAccountStatus())) {
                    UserController.emailTokens.remove(email);
                    throw new RuntimeException(ExceptionMessage.INACTIVE_ACCOUNT);
                }

                return true;
            }
        }
        if (method.isAnnotationPresent(AdminLoginToken.class)) {
            AdminLoginToken adminLoginToken = method.getAnnotation(AdminLoginToken.class);
            if (adminLoginToken.required()) {
                if (UserRole.USER.equals(user.getUserRole())) {
                    UserController.emailTokens.remove(email);
                    throw new RuntimeException(ExceptionMessage.NOT_ADMIN);
                }

                return true;
            }
        }
        if (method.isAnnotationPresent(SuperAdminLoginToken.class)) {
            SuperAdminLoginToken superAdminLoginToken = method.getAnnotation(SuperAdminLoginToken.class);
            if (superAdminLoginToken.required()) {
                if (!UserRole.SUPER_ADMIN.equals(user.getUserRole())
                        || !InputMessage.SUPER_ADMIN_EMAIL.equals(email)) {
                    UserController.emailTokens.remove(email);
                    throw new RuntimeException(ExceptionMessage.NOT_SUPER_ADMIN);
                }

                return true;
            }
        }
        return true;
    }


}

