package com.gorillamusic.entity.enums;


public enum CommendTypeEnum {
    NOT_COMMEND(0, "未推荐"),
    COMMEND(1, "已推荐");

    private Integer typpe;
    private String desc;

    CommendTypeEnum(Integer typpe,String desc) {
        this.desc = desc;
        this.typpe = typpe;
    }

    public Integer getType() {
        return typpe;
    }

    public String getDesc() {
        return desc;
    }
}
