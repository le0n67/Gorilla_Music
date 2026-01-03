package com.gorillamusic.service.impl;

import java.util.Date;
import java.util.List;

import com.gorillamusic.entity.constants.Constants;
import com.gorillamusic.entity.dto.TokenUserInfoDTO;
import com.gorillamusic.entity.enums.UserStatusEnum;
import com.gorillamusic.exception.BusinessException;
import com.gorillamusic.redis.RedisComponent;
import com.gorillamusic.utils.CopyTools;
import com.gorillamusic.utils.FileUtils;
import jakarta.annotation.Resource;

import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import com.gorillamusic.entity.enums.PageSize;
import com.gorillamusic.entity.query.UserInfoQuery;
import com.gorillamusic.entity.po.UserInfo;
import com.gorillamusic.entity.vo.PaginationResultVO;
import com.gorillamusic.entity.query.SimplePage;
import com.gorillamusic.mappers.UserInfoMapper;
import com.gorillamusic.service.UserInfoService;
import com.gorillamusic.utils.StringTools;
import org.springframework.util.IdGenerator;


/**
 * 用户信息 业务接口实现
 */
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {

    @Resource
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;
    @Resource
    private FileUtils fileUtils;
    @Resource
    private RedisComponent redisComponent;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<UserInfo> findListByParam(UserInfoQuery param) {
        return this.userInfoMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(UserInfoQuery param) {
        return this.userInfoMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResultVO<UserInfo> findListByPage(UserInfoQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<UserInfo> list = this.findListByParam(param);
        PaginationResultVO<UserInfo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(UserInfo bean) {
        return this.userInfoMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<UserInfo> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.userInfoMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<UserInfo> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.userInfoMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(UserInfo bean, UserInfoQuery param) {
        StringTools.checkParam(param);
        return this.userInfoMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(UserInfoQuery param) {
        StringTools.checkParam(param);
        return this.userInfoMapper.deleteByParam(param);
    }

    /**
     * 根据UserId获取对象
     */
    @Override
    public UserInfo getUserInfoByUserId(String userId) {
        return this.userInfoMapper.selectByUserId(userId);
    }

    /**
     * 根据UserId修改
     */
    @Override
    public Integer updateUserInfoByUserId(UserInfo bean, String userId) {
        return this.userInfoMapper.updateByUserId(bean, userId);
    }

    /**
     * 根据UserId删除
     */
    @Override
    public Integer deleteUserInfoByUserId(String userId) {
        return this.userInfoMapper.deleteByUserId(userId);
    }

    /**
     * 根据Email获取对象
     */
    @Override
    public UserInfo getUserInfoByEmail(String email) {
        return this.userInfoMapper.selectByEmail(email);
    }

    /**
     * 根据Email修改
     */
    @Override
    public Integer updateUserInfoByEmail(UserInfo bean, String email) {
        return this.userInfoMapper.updateByEmail(bean, email);
    }

    /**
     * 根据Email删除
     */
    @Override
    public Integer deleteUserInfoByEmail(String email) {
        return this.userInfoMapper.deleteByEmail(email);
    }

    @Override
    public void register(String email, String nickName, String password) {
        if (this.userInfoMapper.selectByEmail(email) != null) {
            throw new BusinessException("邮箱已存在");
        }
        Date curDate = new Date();
        UserInfo userInfo = new UserInfo();
        String userId = StringTools.getRandomNumber(Constants.LENGTH_12);
        userInfo.setUserId(userId);
        userInfo.setEmail(email);
        userInfo.setNickName(nickName);
        userInfo.setPassword(StringTools.encodeByMD5(password));
        userInfo.setCreateTime(curDate);
        userInfo.setStatus(UserStatusEnum.ENABLE.getStatus());
        userInfo.setAvatar(fileUtils.copyAvatar(userId));
        this.userInfoMapper.insert(userInfo);
    }

    @Override
    public TokenUserInfoDTO login(String email, String password) {
        UserInfo userInfo = this.userInfoMapper.selectByEmail(email);
        if (userInfo == null || !userInfo.getPassword().equals(password)) {
            throw new BusinessException("账号或密码错误");
        }
        if (UserStatusEnum.DISABLE.getStatus().equals(userInfo.getStatus())) {
            throw new BusinessException("账号被禁用");
        }

        UserInfo updateUserInfo = new UserInfo();
        updateUserInfo.setLastLoginTime(new Date());
        this.userInfoMapper.updateByUserId(updateUserInfo, updateUserInfo.getUserId());
        TokenUserInfoDTO tokenUserInfoDTO = CopyTools.copy(userInfo, TokenUserInfoDTO.class);
        String token = StringTools.encodeByMD5(tokenUserInfoDTO.getUserId()+StringTools.getRandomNumber(Constants.LENGTH_20));
        tokenUserInfoDTO.setToken(token);
        redisComponent.saveTokenUserInfoDto(tokenUserInfoDTO);
        return tokenUserInfoDTO;
    }
}



































