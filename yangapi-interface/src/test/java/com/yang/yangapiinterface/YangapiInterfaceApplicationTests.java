package com.yang.yangapiinterface;

import com.yang.yangapiclientsdk.client.YangApiClient;
import com.yang.yangapiclientsdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class YangapiInterfaceApplicationTests {

    @Resource
    private YangApiClient yangApiClient;

    @Test
    void contextLoads() {
        String result = yangApiClient.getNameByGet("yang");
        User user = new User();
        user.setUsername("yang");
        String userNameByPost = yangApiClient.getUserNameByPost(user);
        System.out.println(result);
        System.out.println(userNameByPost);
    }

    @Test
    public void aa(){
        String yang = new String("yang");
        System.out.println(yang);
    }

}
