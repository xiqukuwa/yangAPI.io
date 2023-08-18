package com.yang.yangapiclientsdk;

import com.yang.yangapiclientsdk.client.YangApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("yangapi.client")
@Data
@ComponentScan
public class YangApiClientConfig {

    private String accessKey;
    private String secretKey;

    @Bean
    public YangApiClient yangApiClient(){
        return new YangApiClient(accessKey,secretKey);
    }

}
