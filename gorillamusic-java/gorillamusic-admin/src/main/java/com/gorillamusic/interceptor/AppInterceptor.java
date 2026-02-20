package com.gorillamusic.interceptor;

import com.gorillamusic.entity.dto.TokenUserInfo4AdminDTO;
import com.gorillamusic.entity.enums.ResponseCodeEnum;
import com.gorillamusic.entity.vo.PaginationResultVO;
import com.gorillamusic.exception.BusinessException;
import com.gorillamusic.redis.RedisComponent;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Date：2026/2/20  13:11
 * Description：TODO
 *
 * @author Leon
 * @version 1.0
 */

public class AppInterceptor implements HandlerInterceptor {

    @Resource
    private RedisComponent redisComponent;

    private static final String URL_ACCOUNT = "/account";
    private static final String URL_FILE = "/file ";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (null == handler) {
            return false;
        }
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        return checkLogin();
    }

    private Boolean checkLogin() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String url = request.getRequestURI();
        if (url.indexOf(URL_ACCOUNT) != -1 || url.indexOf(URL_FILE) != -1) {
            return true;
        }

        String token = request.getHeader("token");
        TokenUserInfo4AdminDTO tokenUserInfo4AdminDTO = redisComponent.getTokenUserInfo4AdminDTO(token);
        if(tokenUserInfo4AdminDTO == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
