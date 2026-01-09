package com.gorillamusic.service.impl;

import java.util.List;

import com.gorillamusic.entity.po.UserInfo;
import com.gorillamusic.entity.query.UserInfoQuery;
import com.gorillamusic.mappers.UserInfoMapper;
import jakarta.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gorillamusic.entity.enums.PageSize;
import com.gorillamusic.entity.query.MusicInfoQuery;
import com.gorillamusic.entity.po.MusicInfo;
import com.gorillamusic.entity.vo.PaginationResultVO;
import com.gorillamusic.entity.query.SimplePage;
import com.gorillamusic.mappers.MusicInfoMapper;
import com.gorillamusic.service.MusicInfoService;
import com.gorillamusic.utils.StringTools;


/**
 * 音乐信息 业务接口实现
 */
@Service("musicInfoService")
public class MusicInfoServiceImpl implements MusicInfoService {

    @Resource
    private MusicInfoMapper<MusicInfo, MusicInfoQuery> musicInfoMapper;

    @Resource
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<MusicInfo> findListByParam(MusicInfoQuery param) {
        return this.musicInfoMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(MusicInfoQuery param) {
        return this.musicInfoMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResultVO<MusicInfo> findListByPage(MusicInfoQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<MusicInfo> list = this.findListByParam(param);
        PaginationResultVO<MusicInfo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(MusicInfo bean) {
        return this.musicInfoMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<MusicInfo> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.musicInfoMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<MusicInfo> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.musicInfoMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(MusicInfo bean, MusicInfoQuery param) {
        StringTools.checkParam(param);
        return this.musicInfoMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(MusicInfoQuery param) {
        StringTools.checkParam(param);
        return this.musicInfoMapper.deleteByParam(param);
    }

    /**
     * 根据MusicId获取对象
     */
    @Override
    public MusicInfo getMusicInfoByMusicId(String musicId) {
        MusicInfo musicInfo = this.musicInfoMapper.selectByMusicId(musicId);
        if (musicInfo != null) {
            UserInfo userInfo = this.userInfoMapper.selectByUserId(musicInfo.getUserId());
            musicInfo.setNickName(userInfo.getNickName());
            musicInfo.setAvatar(userInfo.getAvatar());
        }
        return musicInfo;
    }

    /**
     * 根据MusicId修改
     */
    @Override
    public Integer updateMusicInfoByMusicId(MusicInfo bean, String musicId) {
        return this.musicInfoMapper.updateByMusicId(bean, musicId);
    }

    /**
     * 根据MusicId删除
     */
    @Override
    public Integer deleteMusicInfoByMusicId(String musicId) {
        return this.musicInfoMapper.deleteByMusicId(musicId);
    }

    /**
     * 根据TaskId获取对象
     */
    @Override
    public MusicInfo getMusicInfoByTaskId(String taskId) {
        return this.musicInfoMapper.selectByTaskId(taskId);
    }

    /**
     * 根据TaskId修改
     */
    @Override
    public Integer updateMusicInfoByTaskId(MusicInfo bean, String taskId) {
        return this.musicInfoMapper.updateByTaskId(bean, taskId);
    }

    /**
     * 根据TaskId删除
     */
    @Override
    public Integer deleteMusicInfoByTaskId(String taskId) {
        return this.musicInfoMapper.deleteByTaskId(taskId);
    }

    @Override
    public void updateMusicCount(String musicId) {
        this.musicInfoMapper.updateMusicCount(musicId);
    }
}