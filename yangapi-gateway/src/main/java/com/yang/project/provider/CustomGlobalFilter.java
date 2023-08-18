package com.yang.project.provider;


import com.yang.common.model.entity.InterfaceInfo;
import com.yang.common.model.entity.User;
import com.yang.common.service.InnerInterfaceInfoService;
import com.yang.common.service.InnerUserInterfaceInfoService;
import com.yang.common.service.InnerUserService;
import com.yang.yangapiclientsdk.utils.SignUtils;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//全局过滤
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private InnerUserService innerUserService;

    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;

    @DubboReference
    public static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1");

    private static final String INTERFACE_HOST = "http://localhost:8123";


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        1.请求日志
        ServerHttpRequest request = exchange.getRequest();
        String path  = INTERFACE_HOST + request.getPath();
        String method = request.getMethod().toString();
        log.info("请求唯一标识" + request.getId());
        log.info("请求路径" + request.getPath().value());
        log.info("请求方法" + request.getMethod());
        log.info("请求参数" + request.getQueryParams());
        String sourceAddress = request.getLocalAddress().getHostString();
        log.info("请求源地址" + sourceAddress);
        log.info("请求源地址" + request.getRemoteAddress());
        ServerHttpResponse response = exchange.getResponse();

//        2.黑白名单
        if (!IP_WHITE_LIST.contains(sourceAddress)){
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }

//        3.用户鉴权
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce =headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");
        //数据库查是否已经分配给用户
        User invokeUser = null;
        try {
            invokeUser = innerUserService.getInvokeUser(accessKey);
        } catch (Exception e) {
            log.info("getInvokeError",e);
        }
        if (invokeUser == null){
            handleNoAuth(response);
        }

        //验证accesskey
//        if (!accessKey.equals("123456")){
//            handleNoAuth(response);
//        }

        if (Long.parseLong(nonce) > 10000){
            handleNoAuth(response);
        }
        //距离当前时间不超过五分钟
        Long currentTimeMillis = System.currentTimeMillis() / 1000;
        final Long FIVE_MINUTES = 60 * 5l;
        if ((currentTimeMillis - Long.parseLong(timestamp)) >= FIVE_MINUTES){
            handleNoAuth(response);
        }
        //实际上是在数据库查出secretkey
        String secretKey = invokeUser.getSecretKey();
        String serverSign = SignUtils.getSign(body, secretKey);
        if (sign == null || !sign.equals(serverSign)){
            handleNoAuth(response);
        }

//        4.判断模拟的接口是否存在?
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(path, method);
        } catch (Exception e) {
            log.info("getInterfaceInfo",e);
        }
        if (interfaceInfo == null){
            handleNoAuth(response);
        }
//        5.请求转发，调用模拟接口
//        Mono<Void> filter = chain.filter(exchange);

//        6.响应日志+调用接口+请求转发
        return handleResponse(exchange,chain,interfaceInfo.getId(),invokeUser.getId());

//        7.调用成功。invoke次数+1
//        if (response.getStatusCode() == HttpStatus.OK){
//
//        }else {
//            return HandelInvokeError(response);
//        }
//        return  filter;
    }

    @Override
    public int getOrder() {
        return -2;
    }

    public Mono<Void> handleNoAuth(ServerHttpResponse response){
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public Mono<Void> HandelInvokeError(ServerHttpResponse response){
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }

    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain,long interfaceInfoId,long userId) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            HttpStatus statusCode = originalResponse.getStatusCode();
            if(statusCode == HttpStatus.OK){
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        System.out.println("daole");
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            //
                            return super.writeWith(
                             fluxBody.map(dataBuffer -> {
                                //接口调用成功，接口使用次数加一
                                 try {
                                     innerUserInterfaceInfoService.invokeCount(interfaceInfoId,userId);
                                 } catch (Exception e) {
                                     log.info("invokeCount"+e);
                                 }
                                 byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);//释放掉内存
                                // 构建日志
                                StringBuilder sb2 = new StringBuilder(200);
                                List<Object> rspArgs = new ArrayList<>();
                                rspArgs.add(originalResponse.getStatusCode());
                                //rspArgs.add(requestUrl);
                                String data = new String(content, StandardCharsets.UTF_8);//data
                                sb2.append(data);
//                                7.调用成功。invoke次数+1
                                log.info("响应结果" + data);//log.info("<-- {} {}\n", originalResponse.getStatusCode(), data);
                                return bufferFactory.wrap(content);
                            }));
                        } else {
                            log.error("<--- {} 响应   code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange);//降级处理返回数据
        }catch (Exception e){
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
    }
}
