/**
 * FileName: WebsocketServiceImpl
 * Author:   huang.yj
 * Date:     2019/10/9 9:18
 * Description: websocket的业务类
 */
package com.springboot.sample.websocket;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;

/**
 * 〈websocket的业务类〉
 *
 * @author huang.yj
 * @create 2019/10/9
 * @since 1.0.0
 */
@Service("websocketService")
public class WebsocketServiceImpl implements WebsocketService<WebSocketListController>{
    @Override
    public void broadCast(String message, Collection<WebSocketListController> collection) {
        for (WebSocketListController item : collection) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void add(WebSocketListController o, Collection<WebSocketListController> collection) {

    }

    @Override
    public void remove(WebSocketListController o, Collection<WebSocketListController> collection) {

    }
}