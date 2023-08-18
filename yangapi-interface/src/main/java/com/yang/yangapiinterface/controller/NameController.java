package com.yang.yangapiinterface.controller;


import com.yang.yangapiclientsdk.model.User;
import com.yang.yangapiclientsdk.utils.SignUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;


@RestController()
@RequestMapping("/name")
public class NameController {


    @GetMapping("/get")
    public String getNameByGet(String name){

        return "get你的名字是：" + name;
    }


    @PostMapping("/post")
    public String getNameByPost(@RequestParam String name){
        return "post你的名字是：" + name;
    }

    @PostMapping("/user")
    public String getUserNameByPost(@RequestBody User user, HttpServletRequest request) throws ParseException {

//        String accessKey = request.getHeader("accessKey");
//        String nonce = request.getHeader("nonce");
//        String timestamp = request.getHeader("timestamp");
//        String sign = request.getHeader("sign");
//        String body = request.getHeader("body");
//
//        if (!accessKey.equals("123456")){
//            throw new RuntimeException("无权限");
//        }
//
//        if (Long.parseLong(nonce) > 10000){
//            throw new RuntimeException("无权限");
//        }
//
//        String serverSign = SignUtils.getSign(body, "123456");
//        if (!sign.equals(serverSign)){
//            throw new RuntimeException("无权限");
//        }

        return "用户名是：" + user.getUsername();
    }
}
