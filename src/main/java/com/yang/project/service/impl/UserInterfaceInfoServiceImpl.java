package com.yang.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.yang.common.model.entity.UserInterfaceInfo;

import com.yang.project.common.ErrorCode;
import com.yang.project.exception.BusinessException;
import com.yang.project.mapper.UserInterfaceInfoMapper;
import com.yang.project.service.UserInterfaceInfoService;
import org.springframework.stereotype.Service;

/**
* @author 86176
* @description 针对表【user_interface_info(用户调用接口信息)】的数据库操作Service实现
* @createDate 2023-06-05 10:37:13
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService {

    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        if (add) {
            if (userInterfaceInfo.getInterfaceInfoId() <= 0 || userInterfaceInfo.getUserId()<=0){
                throw  new BusinessException(ErrorCode.PARAMS_ERROR,"接口或用户不存在");
            }
        }
        if (userInterfaceInfo.getLeftNum() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "剩余次数不能少于0");
        }

    }



    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        if (interfaceInfoId <= 0 || userId <= 0){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("interfaceInfoId",interfaceInfoId);
        updateWrapper.eq("userId",userId);
        updateWrapper.setSql("leftNum = leftNum - 1,totalNum = totalNum + 1");
        return this.update(updateWrapper);
    }



}




