package com.mzy.init.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mzy.init.web.model.entity.RequestLog;
import com.mzy.init.web.model.entity.User;
import com.mzy.init.web.repository.RequestLogRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class RequestLoggingAspect {

    @Autowired
    private RequestLogRepository logRepository;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpSession session;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Around("execution(* com.mzy.init.web.controller..*(..))")
    public Object logRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        User user = (User) session.getAttribute("user");
        Long userId = user != null ? user.getId() : null;

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String clientIp = request.getRemoteAddr();

        Map<String, Object> params = new HashMap<>();

        // 处理 GET 请求参数
        if ("GET".equalsIgnoreCase(method)) {
            request.getParameterMap().forEach((key, value) -> params.put(key, value[0]));
        }

        // 处理 POST 请求参数
        if ("POST".equalsIgnoreCase(method)) {
            // 获取方法参数注解，检查是否有 @RequestBody 注解
            Annotation[][] paramAnnotations = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameterAnnotations();
            Object[] args = joinPoint.getArgs();

            for (int i = 0; i < args.length; i++) {
                for (Annotation annotation : paramAnnotations[i]) {
                    if (annotation instanceof RequestBody) {
                        params.put("body", args[i]);
                    }
                }
            }
        }

        // 将参数转为 JSON 字符串
        String parametersJson = objectMapper.writeValueAsString(params);

        RequestLog requestLog = new RequestLog();
        requestLog.setUserId(userId);
        requestLog.setMethod(method);
        requestLog.setUri(uri);
        requestLog.setParameters(parametersJson);
        requestLog.setDetails("Client IP: " + clientIp);

        Object result;
        try {
            result = joinPoint.proceed();
            requestLog.setStatusCode(200);  // 假设成功状态码为 200
        } catch (Exception ex) {
            requestLog.setStatusCode(500);  // 假设失败状态码为 500
            throw ex;
        } finally {
            logRepository.save(requestLog);
        }
        return result;
    }
}
