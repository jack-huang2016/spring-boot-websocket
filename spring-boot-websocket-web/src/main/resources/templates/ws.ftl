<!DOCTYPE html>
<html lang="cn" xml:th="http://www.thymeleaf.org" xmlns:link="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8"/>
    <title>Title</title>
    <script src="http://cdn.bootcss.com/sockjs-client/1.1.4/sockjs.min.js"></script>
    <script src="https://cdn.bootcss.com/stomp.js/2.3.3/stomp.js"></script>
    <script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
</head>
<body onload="disconnect()">

<div>
    <button id="connect" onclick="connect()">连接</button>
    <button id="disconnect" disabled="disabled" onclick="disconnect()">断开连接</button>

</div>

<div id="conversationDiv">
    <label>输入你的名字</label>
    <input type="text" id="name"/>
    <button id="sendName" onclick="sendName()">发送</button>
    <p id="response"></p>
</div>

</body>
<script>
    var stompClient = null;
    function  setConnected(connected) {
        document.getElementById("connect").disabled = connected;
        document.getElementById('disconnect').disabled = !connected;
        document.getElementById('conversationDiv').style.visibility = connected ? 'visible' : 'hidden';
        $('#response').html();
    }

    function  connect() {
        var socket = new SockJS('/myEndpoint');   // 表示连接的SockJS的endpoint名称为/myEndpoint
        stompClient = Stomp.over(socket);  // 表示使用stomp协议来创建WebSocket客户端
        stompClient.connect({},function (frame) {  // 连接服务端
            setConnected(true);
            console.log('connected'+ frame);
            // 通过stompClient.subscribe订阅/topic/getResponse目标发送的消息，这个是在控制器的@SendTo中定义的
            stompClient.subscribe('/topic/getResponse',function (response) {
                showResponse(JSON.parse(response.body).responseMessage);
            });
        });
    }

    function disconnect() {
        if(stompClient!=null){
            stompClient.disconnect();
        }
        setConnected(false);
        console.log('disconnected')
    }


    function  sendName() {
        var name = $('#name').val();
		// 通过stompClient.send向/welcome目标发送消息，这个是在控制器的@MessageMapping中定义的
        stompClient.send('/welcome',{},JSON.stringify({'name':name}));
    }
    function  showResponse(message) {
        var response = $('#response');
        response.html(message);
    }

</script>
</html>