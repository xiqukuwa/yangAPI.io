package com.yang.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.common.model.entity.InterfaceInfo;

/**
* @author 86176
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2023-05-23 21:14:22
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

}
