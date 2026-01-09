package com.gorillamusic.controller;

import com.gorillamusic.entity.enums.CommendTypeEnum;
import com.gorillamusic.entity.enums.MusicStatusEnum;
import com.gorillamusic.entity.enums.PageSize;
import com.gorillamusic.entity.po.MusicInfo;
import com.gorillamusic.entity.query.MusicInfoQuery;
import com.gorillamusic.entity.vo.ResponseVO;
import com.gorillamusic.service.MusicInfoService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import com.gorillamusic.entity.vo.PaginationResultVO;
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


    @RequestMapping("/loadLatestMusic")
    public ResponseVO loadLatestMusic(Integer pageNo, Integer indexType) {
        MusicInfoQuery query = new MusicInfoQuery();
        query.setCommendType(CommendTypeEnum.NOT_COMMEND.getTyppe());
        query.setMusicStatus(MusicStatusEnum.CREATED.getStatus());
        query.setQueryUser(true);
        query.setOrderBy("m.create_time desc");
        if (indexType != null) {
            query.setPageSize(PageSize.SIZE15.getSize());
        } else {
            query.setPageSize(PageSize.SIZE20.getSize());
        }
        query.setPageNo(pageNo);
        PaginationResultVO list = musicInfoService.findListByPage(query);
        return getSuccessResponseVO(list);
    }

    @RequestMapping("/musicDetail")
    public ResponseVO musicDetail(@NotEmpty String musicId) {
        MusicInfo musicInfo = musicInfoService.getMusicInfoByMusicId(musicId);
        return getSuccessResponseVO(musicInfo);
    }

    @RequestMapping("/updatePlayCount")
    public ResponseVO updatePlayCount(@NotEmpty String musicId) {
        musicInfoService.updateMusicCount(musicId);
        return getSuccessResponseVO(null);
    }

}