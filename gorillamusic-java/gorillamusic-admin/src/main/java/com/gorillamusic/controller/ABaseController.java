package com.gorillamusic.controller;

import com.gorillamusic.entity.dto.TokenUserInfo4AdminDTO;
import com.gorillamusic.entity.dto.TokenUserInfoDTO;
import com.gorillamusic.entity.enums.ResponseCodeEnum;
import com.gorillamusic.entity.vo.ResponseVO;
import com.gorillamusic.exception.BusinessException;
import com.gorillamusic.redis.RedisComponent;
import com.gorillamusic.utils.StringTools;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


public class ABaseController {

    protected static final String STATUC_SUCCESS = "success";

    protected static final String STATUC_ERROR = "error";

    @Resource
    private RedisComponent redisComponent;

    protected <T> ResponseVO getSuccessResponseVO(T t) {
        ResponseVO<T> responseVO = new ResponseVO<>();
        responseVO.setStatus(STATUC_SUCCESS);
        responseVO.setCode(ResponseCodeEnum.CODE_200.getCode());
        responseVO.setInfo(ResponseCodeEnum.CODE_200.getMsg());
        responseVO.setData(t);
        return responseVO;
    }

    protected <T> ResponseVO getBusinessErrorResponseVO(BusinessException e, T t) {
        ResponseVO vo = new ResponseVO();
        vo.setStatus(STATUC_ERROR);
        if (e.getCode() == null) {
            vo.setCode(ResponseCodeEnum.CODE_600.getCode());
        } else {
            vo.setCode(e.getCode());
        }
        vo.setInfo(e.getMessage());
        vo.setData(t);
        return vo;
    }

    protected <T> ResponseVO getServerErrorResponseVO(T t) {
        ResponseVO vo = new ResponseVO();
        vo.setStatus(STATUC_ERROR);
        vo.setCode(ResponseCodeEnum.CODE_500.getCode());
        vo.setInfo(ResponseCodeEnum.CODE_500.getMsg());
        vo.setData(t);
        return vo;
    }

    protected TokenUserInfoDTO getLoginUserInfo(String token) {
        if (StringTools.isEmpty(token)) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            token = request.getHeader("token");
        }
        TokenUserInfoDTO tokenUserInfoDTO = redisComponent.getTokenUserInfoDto(token);
        return tokenUserInfoDTO;
    }

    protected TokenUserInfo4AdminDTO getTokenUserInfo(String token) {
        if (StringTools.isEmpty(token)) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            token = request.getHeader("token");
        }
        if (StringTools.isEmpty(token)) {
            return null;
        }
        TokenUserInfo4AdminDTO dto = redisComponent.getTokenUserInfo4AdminDTO(token);
        return dto;
    }
}
