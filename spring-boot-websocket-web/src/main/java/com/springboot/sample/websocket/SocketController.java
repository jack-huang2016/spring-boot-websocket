/**
 * FileName: SocketController
 * Author:   huang.yj
 * Date:     2019/10/12 9:40
 * Description: WebSocket控制器，使用了该类就不要使用同包下的其他controll
 */
package com.springboot.sample.websocket;

import com.springboot.sample.base.dto.SocketMessage;
import com.springboot.sample.base.dto.SocketResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * 〈WebSocket控制器，使用了该类就不要使用同包下的其他controll〉
 *  当一个浏览器发送一个消息到服务器端的时候，其他浏览器也能收到从服务器端发送来的消息。
 *
 * @author huang.yj
 * @create 2019/10/12
 * @since 1.0.0
 */
@Controller
public class SocketController {
    // 当浏览器向服务器发送请求的时候通过@MessageMapping 映射/welcome这个地址，类似于RequestMapping
    @MessageMapping("/welcome")
    // 当服务器有消息的时候，会对订阅了@SendTo中的路径浏览器发送消息
    @SendTo("/topic/getResponse")
    public SocketResponse say(SocketMessage socketMessage) throws InterruptedException {
        Thread.sleep(3000);
        return new SocketResponse("Welcome," + socketMessage.getName());
    }
}