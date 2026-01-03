package com.gorillamusic.entity.constants;

/**
 * Date：2026/1/3  10:40
 * Description：TODO
 *
 * @author Leon
 * @version 1.0
 */

/**
 * 常量类，定义系统中使用的各种常量值
 */
public class Constants {
    /**
     * Redis键过期时间常量，表示1分钟的秒数
     */
    public static final Long REDIS_KEY_EXPRESS_ONE_MIN = 60L;

    /**
     * Redis键前缀常量，用于标识GorillaMusic系统的缓存键
     */
    public static final String REDIS_KEY_PREFIX = "gorillamusic:";

    /**
     * Redis验证码校验键前缀，用于存储验证码相关的缓存键
     */
    public static final String REDIS_KEY_CHECK_CODE = REDIS_KEY_PREFIX + "checkcode:";
}
