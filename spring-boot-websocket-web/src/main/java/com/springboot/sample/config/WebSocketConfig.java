/**
 * FileName: WebSocketConfig
 * Author:   huang.yj
 * Date:     2019/9/27 16:49
 * Description: WebSocket配置类
 */
package com.springboot.sample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;



/**
 * 〈WebSocket配置类〉
 *
 * @author huang.yj
 * @create 2019/9/27
 * @since 1.0.0
 */
@Configuration
public class WebSocketConfig {
    //注入ServerEndpointExporter，这个bean会自动注册使用了@ServerEndpoint注解声明的Websocket endpoint
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}