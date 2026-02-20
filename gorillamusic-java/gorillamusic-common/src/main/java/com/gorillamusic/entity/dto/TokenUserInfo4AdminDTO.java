package com.gorillamusic.entity.dto;

/**
 * Date：2026/1/3  13:41
 * Description：TODO
 *
 * @author Leon
 * @version 1.0
 */

public class TokenUserInfo4AdminDTO {
    /**
     * 用户ID
     */
    private String account;
    /**
     * 令牌
     */
    private String token;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
