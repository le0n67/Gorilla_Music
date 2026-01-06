package com.gorillamusic.controller;

import com.gorillamusic.entity.dto.TokenUserInfoDTO;
import com.gorillamusic.entity.enums.CommendTypeEnum;
import com.gorillamusic.entity.po.MusicInfo;
import com.gorillamusic.entity.query.MusicInfoQuery;
import com.gorillamusic.entity.vo.CheckCodeVO;
import com.gorillamusic.entity.vo.ResponseVO;
import com.gorillamusic.exception.BusinessException;
import com.gorillamusic.redis.RedisComponent;
import com.gorillamusic.service.MusicInfoService;
import com.gorillamusic.service.UserInfoService;
import com.wf.captcha.ArithmeticCaptcha;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 音乐信息 Controller
 */
@RestController
@RequestMapping("/music")
@Slf4j
@Validated
public class MusicController extends ABaseController {

    @Resource
    private MusicInfoService musicInfoService;


    @RequestMapping("/loadCommendMusic")
    public ResponseVO loadCommendMusic() {
        MusicInfoQuery query = new MusicInfoQuery();
        query.setCommendType(CommendTypeEnum.COMMEND.getTyppe());
        query.setOrderBy("m.create_time desc");
        query.setQueryUser(true);
        List<MusicInfo> list = musicInfoService.findListByParam(query);
        return getSuccessResponseVO(list);
    }


}