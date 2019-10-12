/**
 * FileName: SocketResponse
 * Author:   huang.yj
 * Date:     2019/10/12 9:38
 * Description: 服务器向浏览器发送此类的消息
 */
package com.springboot.sample.base.dto;

/**
 * 〈服务器向浏览器发送此类的消息〉
 *
 * @author huang.yj
 * @create 2019/10/12
 * @since 1.0.0
 */
public class SocketResponse {
    private String responseMessage;

    public String getResponseMessage() {
        return responseMessage;
    }

    public SocketResponse(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}