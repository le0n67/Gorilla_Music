package com.gorillamusic.entity.dto;

/**
 * Date：2026/1/3  13:41
 * Description：TODO
 *
 * @author Leon
 * @version 1.0
 */

public class TokenUserInfoDTO {
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 令牌
     */
    private String token;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 积分
     */
    private Integer integral;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
