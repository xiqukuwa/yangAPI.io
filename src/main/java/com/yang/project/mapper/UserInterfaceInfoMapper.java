package com.yang.project.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yang.common.model.entity.UserInterfaceInfo;

import java.util.List;

/**
* @author 86176
* @description 针对表【user_interface_info(用户调用接口信息)】的数据库操作Mapper
* @createDate 2023-06-05 10:37:13
* @Entity generator.domain.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);
}




