/**
 * FileName: WebSocketCollectionController
 * Author:   huang.yj
 * Date:     2019/9/27 14:05
 * Description: Demo1: 线程安全的Set或者List来存放每个客户端对应的WebSocket Server对象
 */
package com.springboot.sample.websocket;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 〈Demo1: 线程安全的Set或者List来存放每个客户端对应的WebSocket Server对象〉
 *  @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 *  注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 *
 * @author huang.yj
 * @create 2019/9/27
 * @since 1.0.0
 */
@Component
@ServerEndpoint("/websocket/collection")
public class WebSocketCollectionController {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    //concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketCollectionController对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static CopyOnWriteArraySet<WebSocketCollectionController> webSocketSet = new CopyOnWriteArraySet<WebSocketCollectionController>();

    // 也可以不使用Set存储，而使用线程安全List存储，存放每个客户端对应的WebSocketCollectionController对象。
    // private static Collection<WebSocketCollectionController> webSocketList = Collections.synchronizedCollection(new ArrayList<WebSocketCollectionController>());

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

/**
       * 连接建立成功调用的方法
       * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
       */
     @OnOpen
     public void onOpen(Session session){
         this.session = session;
         webSocketSet.add(this);     //加入set中
        // webSocketList.add(this);     //加入list中
         addOnlineCount();           //在线数加1
         System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
     }

    /**
     * 表示浏览器发出关闭请求的时候被调用
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        // webSocketList.remove(this);     //从list中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 服务端收到客户端消息的时候被调用
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
        //群发消息
        // for (WebSocketCollectionController item : webSocketList) {
        for (WebSocketCollectionController item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);  //同步、阻塞式的
        //this.session.getAsyncRemote().sendText(message);  //异步、非阻塞式的、推荐使用
    }

    public static int getOnlineCount() {
        return WebSocketCollectionController.onlineCount.get();
    }

    public static void addOnlineCount() {
        WebSocketCollectionController.onlineCount.incrementAndGet();
    }

    public static void subOnlineCount() {
        WebSocketCollectionController.onlineCount.decrementAndGet();
    }
}