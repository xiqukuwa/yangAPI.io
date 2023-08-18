package com.yang.project.provider;

import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;

@DubboService
public class DemoServiceImpl implements DemoService{

    @Override
    public String sayHello(String name) {
        System.out.println("hello" + name + ",request form consumer:" + RpcContext.getContext().getRemoteAddressString());
        return "hello" + name;
    }

    @Override
    public String sayHello2(String name) {
        return "yang";
    }
}
