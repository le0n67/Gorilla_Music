package com.gorillamusic.controller;

import com.gorillamusic.entity.config.AppConfig;
import com.gorillamusic.entity.constants.Constants;
import com.gorillamusic.entity.dto.TokenUserInfo4AdminDTO;
import com.gorillamusic.entity.dto.TokenUserInfoDTO;
import com.gorillamusic.entity.vo.CheckCodeVO;
import com.gorillamusic.entity.vo.ResponseVO;
import com.gorillamusic.exception.BusinessException;
import com.gorillamusic.redis.RedisComponent;
import com.gorillamusic.service.UserInfoService;
import com.gorillamusic.utils.StringTools;
import com.wf.captcha.ArithmeticCaptcha;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 账号信息 Controller
 */
@RestController
@RequestMapping("/account")
@Slf4j
@Validated
public class AccountController extends ABaseController {

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private AppConfig appConfig;

    @RequestMapping("/checkCode")
    public ResponseVO checkCode(HttpServletRequest request) {
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(100, 42);
        String code = captcha.text();
        String checkCodeKey = redisComponent.saveCheckCode(code);
        String checkCodeBase64 = captcha.toBase64();
        CheckCodeVO checkCodeVO = new CheckCodeVO();
        checkCodeVO.setCheckCode(checkCodeBase64);
        checkCodeVO.setCheckCodeKey(checkCodeKey);
        return getSuccessResponseVO(checkCodeVO);
    }


    @RequestMapping("/login")
    public ResponseVO login(@NotEmpty String checkCodeKey,
                            @NotEmpty String checkCode,
                            @NotEmpty String account,
                            @NotEmpty String password) {
        try {
            if (!redisComponent.getCheckCode(checkCodeKey).equals(checkCode)) {
                throw new BusinessException("验证码错误");
            }

            if (!account.equals(appConfig.getAccount()) || !password.equals(StringTools.encodeByMD5(appConfig.getPassword()))) {
                throw new BusinessException("账号密码错误");
            }

            TokenUserInfo4AdminDTO adminDTO = new TokenUserInfo4AdminDTO();
            adminDTO.setAccount(account);
            adminDTO.setToken(StringTools.getRandomNumber(Constants.LENGTH_30));
            redisComponent.saveTokenUserInfo4AdminDTO(adminDTO);
            return getSuccessResponseVO(adminDTO.getToken());
        } finally {
            redisComponent.cleanCheckCode(checkCodeKey);
        }
    }

    @RequestMapping(value = "/logout")
    public ResponseVO logout() {
        TokenUserInfo4AdminDTO dto = getTokenUserInfo(null);
        if (dto == null) {
            return getSuccessResponseVO(null);
        }
        redisComponent.cleanAdminUserToken(dto.getToken());
        return getSuccessResponseVO(null);
    }

}