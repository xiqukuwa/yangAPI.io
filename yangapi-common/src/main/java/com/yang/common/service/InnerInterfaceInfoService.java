package com.yang.common.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.common.model.entity.InterfaceInfo;
import com.yang.common.model.entity.User;

/**
* @author 86176
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2023-05-23 21:14:22
*/
public interface InnerInterfaceInfoService  {



    //    2.用户查询模拟接口是否存在

    InterfaceInfo getInterfaceInfo(String path ,String method);


}
