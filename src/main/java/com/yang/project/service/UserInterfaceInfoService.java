package com.yang.project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.common.model.entity.UserInterfaceInfo;

/**
* @author 86176
* @description 针对表【user_interface_info(用户调用接口信息)】的数据库操作Service
* @createDate 2023-06-05 10:37:13
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    boolean invokeCount(long interfaceInfoId,long userId);
}
