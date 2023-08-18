package com.yang.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yang.common.model.entity.InterfaceInfo;
import com.yang.common.model.entity.User;
import com.yang.common.service.InnerInterfaceInfoService;
import com.yang.project.common.ErrorCode;
import com.yang.project.exception.BusinessException;
import com.yang.project.mapper.InterfaceInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {
    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    @Override
    public InterfaceInfo getInterfaceInfo(String url, String method) {
        if (StringUtils.isAnyBlank(url,method) ){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url",url);
        queryWrapper.eq("method",method);
        InterfaceInfo interfaceInfo = interfaceInfoMapper.selectOne(queryWrapper);
        return interfaceInfo;
    }
}
