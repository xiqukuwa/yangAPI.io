package com.yang.common.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.common.model.entity.User;


/**
 * 用户服务
 *
 * @author yang
 */
public interface InnerUserService {

    //    1.数据库中是否分配给用户密钥
    User getInvokeUser(String accessKey);


}
