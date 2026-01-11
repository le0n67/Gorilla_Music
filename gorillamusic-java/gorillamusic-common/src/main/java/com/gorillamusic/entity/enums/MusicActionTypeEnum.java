package com.gorillamusic.entity.enums;

/**
 * Date：2026/1/11  12:23
 * Description：TODO
 *
 * @author Leon
 * @version 1.0
 */
public enum MusicActionTypeEnum {

    GOOD(1, "点赞"),
    COMMENT(2, "评论"),
    COLLECT(3, "收藏"),
    SHARE(4, "分享"),
    ;

    private Integer type;
    private String desc;

    MusicActionTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
