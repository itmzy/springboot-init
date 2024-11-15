package com.mzy.init.aop;

import com.mzy.init.annotation.RoleRequired;
import com.mzy.init.exception.CustomException;
import com.mzy.init.web.model.entity.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Aspect
@Component
public class AuthAspect {

    private final HttpSession session;

    public AuthAspect(HttpSession session) {
        this.session = session;
    }

    // 登录校验，支持类级和方法级的 @LoginRequired 注解
    @Around("@within(com.mzy.init.annotation.LoginRequired) || @annotation(com.mzy.init.annotation.LoginRequired)")
    public Object checkLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new CustomException(401, "User not logged in");
        }
        return joinPoint.proceed();
    }

    // 权限校验
    @Around("@annotation(roleRequired)")
    public Object checkRole(ProceedingJoinPoint joinPoint, RoleRequired roleRequired) throws Throwable {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new CustomException(401, "User not logged in");
        }
        if (!user.getRole().equals(roleRequired.value())) {
            throw new CustomException(403, "User does not have required role: " + roleRequired.value());
        }
        return joinPoint.proceed();
    }
}
