package com.gorillamusic.aspect;

import com.gorillamusic.annotation.GlobalInterceptor;
import com.gorillamusic.entity.dto.TokenUserInfoDTO;
import com.gorillamusic.entity.enums.ResponseCodeEnum;
import com.gorillamusic.exception.BusinessException;
import com.gorillamusic.redis.RedisComponent;
import com.gorillamusic.utils.StringTools;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletSecurityElement;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

/**
 * Date：2026/1/9  23:32
 * Description：TODO
 *
 * @author Leon
 * @version 1.0
 */


@Component("globalOperationAspect")
@Aspect
@Slf4j
public class GlobalOperationAspect {

    @Resource
    private RedisComponent redisComponent;

    @Before("@annotation(com.gorillamusic.annotation.GlobalInterceptor)")
    public void interceptorDo(JoinPoint point) {
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        GlobalInterceptor interceptor = method.getAnnotation(GlobalInterceptor.class);
        if (null == interceptor) return;
        if (interceptor.checkLogin()) {
            log.info("checkLogin");
            checkLogin();
        }
    }

    private void checkLogin() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("token");
        if (StringTools.isEmpty(token)) {
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }
//        TokenUserInfoDTO tokenUserInfoDTO = redisComponent.getTokenUserInfoDto(token);
//
//        if (System.getProperty("dev") != null) {
//            tokenUserInfoDTO = new TokenUserInfoDTO();
//            tokenUserInfoDTO.setUserId("000000000");
//            tokenUserInfoDTO.setNickName("dev");
//            tokenUserInfoDTO.setAvatar("https://picsum.photos/200/300");
//            tokenUserInfoDTO.setToken(token);
//            redisComponent.saveTokenUserInfoDto(tokenUserInfoDTO);
//        }
//        if (tokenUserInfoDTO == null) {
//            throw new BusinessException(ResponseCodeEnum.CODE_901);
//        }
    }

}










