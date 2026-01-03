package com.gorillamusic.redis;

import com.gorillamusic.entity.constants.Constants;
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
        redisUtils.setex(Constants.REDIS_KEY_CHECK_CODE, code, Constants.REDIS_KEY_EXPRESS_ONE_MIN * 10);
        return checkCodeKey;
    }

}
