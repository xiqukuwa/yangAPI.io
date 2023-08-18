package com.yang.project.provider;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EnableDubbo
@Service
public class YangapiGatewayApplication {

    @DubboReference
//    @Reference
    private DemoService demoService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(YangapiGatewayApplication.class, args);
        YangapiGatewayApplication application = context.getBean(YangapiGatewayApplication.class);
//        System.out.println(application);
      String result = application.sayHello("world");
       String result2 = application.sayHello2("world");

       System.out.println(result);
       System.out.println(result2);
   }
//
    public String sayHello(String name){
        return demoService.sayHello(name);
    }
    public String sayHello2(String name){
        return demoService.sayHello2(name);

    }
}
