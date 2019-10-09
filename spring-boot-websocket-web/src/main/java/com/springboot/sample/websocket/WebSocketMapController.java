/**
 * FileName: WebSocketMapController
 * Author:   huang.yj
 * Date:     2019/9/27 14:05
 * Description: Demo1: 线程安全的Map来存放每个客户端对应的WebSocket Server对象
 */
package com.springboot.sample.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 〈Demo2: 线程安全的Map来存放每个客户端对应的WebSocket Server对象〉
 *  @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 *  注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 *
 * @author huang.yj
 * @create 2019/9/27
 * @since 1.0.0
 */
@Component
@ServerEndpoint("/websocket/map/{userId}")
public class WebSocketMapController {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    //concurrent包的线程安全Map，用来存放每个客户端对应的WebSocketMapController对象。还能实现服务端与单一客户端的通信，其中Key可以为用户标识
    private static Map<String, WebSocketMapController> webSocketMap = new ConcurrentHashMap<String, WebSocketMapController>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //用户id，用来唯一标识用户
    private String userId;

    /**
       * 连接建立成功调用的方法
       * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
       */
     @OnOpen
     public void onOpen(@PathParam("userId") String userId, Session session){
                 this.userId = userId;
                 this.session = session;
                 webSocketMap.put(userId, this);
                 addOnlineCount();           //在线数加1
                 System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
             }

    /**
     * 表示浏览器发出关闭请求的时候被调用
     */
    @OnClose
    public void onClose() {
        webSocketMap.remove(userId);  //从map中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 服务端收到客户端消息的时候被调用
     *
     * @param mess 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String mess, Session session) {
        JSONObject jsonTo = JSON.parseObject(mess);
        String mes = jsonTo.getString("message");

        if (!jsonTo.get("To").equals("All")){
            sendMessageTo(mes, jsonTo.get("To").toString());
        }else{
            sendMessageAll(mes);
        }
    }

    /**
     * 发生错误时调用,比如网络断开了等等
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    private void sendMessageAll(String message) {
        for (WebSocketMapController item : webSocketMap.values()) {
            item.session.getAsyncRemote().sendText(message);
        }
    }

    private void sendMessageTo(String message, String toUserId) {
        for (WebSocketMapController item : webSocketMap.values()) {
            if (item.userId.equals(toUserId) )
                item.session.getAsyncRemote().sendText(message);
        }
    }

    public static int getOnlineCount() {
        return WebSocketMapController.onlineCount.get();
    }

    public static void addOnlineCount() {
        WebSocketMapController.onlineCount.incrementAndGet();
    }

    public static void subOnlineCount() {
        WebSocketMapController.onlineCount.decrementAndGet();
    }

    public static synchronized Map<String, WebSocketMapController> getWebSocketMap() {
        return webSocketMap;
    }
}