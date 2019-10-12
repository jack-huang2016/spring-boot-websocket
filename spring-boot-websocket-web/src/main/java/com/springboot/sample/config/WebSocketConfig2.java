/**
 * FileName: WebSocketConfig2
 * Author:   huang.yj
 * Date:     2019/10/12 9:22
 * Description: WebSocket配置类2，使用了该类就不要使用同包下的WebSocketConfig
 */
package com.springboot.sample.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * 〈WebSocket配置类2，使用了该类就不要使用同包下的WebSocketConfig〉
 *  注：springboot2.x之前的版本是继承AbstractWebSocketMessageBrokerConfigurer,目前已过时
 *  websocket是通过一个socket来实现双工异步通信功能，但是直接使用websocket会比较麻烦，我们使用它的子协议stomp，
 *  它是一个更高级的协议，stomp使用一个基于帧的格式来定义消息，与http的request和response类似。
 *
 * @author huang.yj
 * @create 2019/10/12
 * @since 1.0.0
 */
@Configuration
@EnableWebSocketMessageBroker  //开启stomp协议来传输基于代理(message broker)的消息
public class WebSocketConfig2 implements WebSocketMessageBrokerConfigurer {

    /**
     * 注册stomp协议的节点(endpoint)，并映射指定的URL
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/myEndpoint")   // 注册一个stomp协议的节点endpoint
                .setAllowedOrigins("*")  // setAllowedOrigins("*")表示可以跨域
                .withSockJS();  // 并使用SocketJS协议
    }

    /**
     * 配置消息代理
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic"); // 广播式应配置一个/topic消息代理
    }
}