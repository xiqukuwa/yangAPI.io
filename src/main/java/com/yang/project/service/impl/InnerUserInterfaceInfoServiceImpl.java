package com.yang.project.service.impl;

import com.yang.common.model.entity.UserInterfaceInfo;
import com.yang.common.service.InnerUserInterfaceInfoService;
import com.yang.project.service.UserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {

        return userInterfaceInfoService.invokeCount(interfaceInfoId,userId);
    }
}
