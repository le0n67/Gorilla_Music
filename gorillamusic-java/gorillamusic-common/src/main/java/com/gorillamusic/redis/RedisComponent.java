package com.gorillamusic.redis;

import com.gorillamusic.entity.constants.Constants;
import com.gorillamusic.entity.dto.TokenUserInfoDTO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Date：2026/1/3  10:31
 * Description：redis具体业务操作
 *
 * @author Leon
 * @version 1.0
 */

@Component
public class RedisComponent {
    @Resource
    private RedisUtils redisUtils;

    public String saveCheckCode(String code) {
        String checkCodeKey = UUID.randomUUID().toString();
        redisUtils.setex(Constants.REDIS_KEY_CHECK_CODE + checkCodeKey, code, Constants.REDIS_KEY_EXPRESS_ONE_MIN * 10);
        return checkCodeKey;
    }

    public String getCheckCode(String checkCodeKey) {
        return (String) redisUtils.get(Constants.REDIS_KEY_CHECK_CODE + checkCodeKey);
    }

    public void cleanCheckCode(String checkCodeKey) {
        redisUtils.delete(Constants.REDIS_KEY_CHECK_CODE + checkCodeKey);
    }

    public void saveTokenUserInfoDto(TokenUserInfoDTO tokenUserInfoDTO) {
        redisUtils.setex(Constants.REDIS_KEY_TOKEN_WEB_USER+tokenUserInfoDTO.getToken(), tokenUserInfoDTO,Constants.REDIS_KEY_EXPRESS_ONE_DAY);
    }

    public TokenUserInfoDTO getTokenUserInfoDto(String token) {
        return (TokenUserInfoDTO) redisUtils.get(Constants.REDIS_KEY_TOKEN_WEB_USER+token);
    }
}
