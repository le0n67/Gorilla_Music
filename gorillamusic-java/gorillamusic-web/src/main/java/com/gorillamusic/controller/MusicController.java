package com.gorillamusic.controller;

import com.gorillamusic.annotation.GlobalInterceptor;
import com.gorillamusic.entity.dto.TokenUserInfoDTO;
import com.gorillamusic.entity.enums.CommendTypeEnum;
import com.gorillamusic.entity.enums.MusicStatusEnum;
import com.gorillamusic.entity.enums.PageSize;
import com.gorillamusic.entity.enums.ResponseCodeEnum;
import com.gorillamusic.entity.po.MusicCreation;
import com.gorillamusic.entity.po.MusicInfo;
import com.gorillamusic.entity.po.MusicInfoAction;
import com.gorillamusic.entity.query.MusicInfoQuery;
import com.gorillamusic.entity.vo.ResponseVO;
import com.gorillamusic.exception.BusinessException;
import com.gorillamusic.redis.RedisComponent;
import com.gorillamusic.service.MusicCreationService;
import com.gorillamusic.service.MusicInfoActionService;
import com.gorillamusic.service.MusicInfoService;
import com.gorillamusic.utils.StringTools;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import com.gorillamusic.entity.vo.PaginationResultVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestHeader;
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

    @Resource
    private MusicInfoActionService musicInfoActionService;

    @Resource
    private MusicCreationService musicCreationService;

    @RequestMapping("/loadCommendMusic")
    public ResponseVO loadCommendMusic() {

        TokenUserInfoDTO tokenUserInfoDTO = getTokenUserInfo(null);
        MusicInfoQuery musicInfoQuery = new MusicInfoQuery();
        musicInfoQuery.setQueryUser(true);
        musicInfoQuery.setCommendType(CommendTypeEnum.COMMEND.getType());
        musicInfoQuery.setCurrentUserId(tokenUserInfoDTO == null ? null : tokenUserInfoDTO.getUserId());
        musicInfoQuery.setOrderBy("m.create_time desc");
        return getSuccessResponseVO(musicInfoService.findListByParam(musicInfoQuery));
    }


    @RequestMapping("/loadLatestMusic")
    public ResponseVO loadLatestMusic(Integer pageNo, Integer indexType) {
        MusicInfoQuery musicInfoQuery = new MusicInfoQuery();
        musicInfoQuery.setQueryUser(true);
        musicInfoQuery.setCommendType(CommendTypeEnum.NOT_COMMEND.getType());
        musicInfoQuery.setMusicStatus(MusicStatusEnum.CREATED.getStatus());
        musicInfoQuery.setOrderBy("m.create_time desc");
        if (indexType != null) {
            musicInfoQuery.setPageSize(PageSize.SIZE15.getSize());
        } else {
            musicInfoQuery.setPageSize(PageSize.SIZE20.getSize());
        }
        TokenUserInfoDTO tokenUserInfoDTO = getTokenUserInfo(null);
        musicInfoQuery.setCurrentUserId(tokenUserInfoDTO == null ? null : tokenUserInfoDTO.getUserId());
        musicInfoQuery.setPageNo(pageNo);
        return getSuccessResponseVO(musicInfoService.findListByPage(musicInfoQuery));
    }

    @RequestMapping("/musicDetail")
    public ResponseVO musicDetail(@NotEmpty String musicId) {
        MusicInfo musicInfo = musicInfoService.getMusicInfoByMusicId(musicId);
        TokenUserInfoDTO tokenUserInfoDTO = getTokenUserInfo(null);
        if (tokenUserInfoDTO != null) {
            MusicInfoAction action = musicInfoActionService.getMusicInfoActionByMusicIdAndUserId(musicId, tokenUserInfoDTO.getUserId());
            musicInfo.setDoGood(action != null);
        }
        return getSuccessResponseVO(musicInfo);
    }

    @RequestMapping("/updatePlayCount")
    public ResponseVO updatePlayCount(@NotEmpty String musicId) {
        musicInfoService.updateMusicCount(musicId);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/doGood")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO doGood(@NotEmpty String musicId) {
        musicInfoActionService.doGood(musicId, getTokenUserInfo(null).getUserId());
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/getCreation")
    public ResponseVO getCreation(@NotEmpty String creationId) {
        MusicCreation musicCreation = musicCreationService.getMusicCreationByCreationId(creationId);
        return getSuccessResponseVO(musicCreation);
    }

}











