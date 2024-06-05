package com.zhj.webrtc;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.json.Json;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@ServerEndpoint(value = "/webRtcTest/{userId}")
@Slf4j
@Component
public class WebRtcTest {
    private static final String GUEST_PREFIX = "Guest";
    private static final AtomicInteger connectionIds = new AtomicInteger(0);
    private static final Set<User> onlineUsers = new CopyOnWriteArraySet<User>();
    private static final String CLIENT_RTC_EVENT = "CLIENT_RTC_EVENT";
    private static final String SERVER_RTC_EVENT = "SERVER_RTC_EVENT";

    private static final String CLIENT_USER_EVENT = "CLIENT_USER_EVENT";
    private static final String SERVER_USER_EVENT = "SERVER_USER_EVENT";

    private static final String CLIENT_USER_EVENT_LOGIN = "CLIENT_USER_EVENT_LOGIN";
    private static final String SERVER_USER_EVENT_UPDATE_USERS = "SERVER_USER_EVENT_UPDATE_USERS";

    private final User user;

    public WebRtcTest() {
        user = new User();
//        user.id = connectionIds.getAndIncrement();
        user.name =  user.id;
        user.candidates = new ArrayList<String>();
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session) throws IOException {
        user.id = userId;
        user.session = session;
        try {
            onlineUsers.add(user);
            System.out.println(user.id + " joined!");
            Map<String,Object> map  = new HashMap<>();
            map.put("type","SERVER_USER_EVENT_UPDATE_USERS");
            map.put("value",getOnlineUser());
            user.session.getBasicRemote().sendText(JSON.toJSONString(map));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void end() {
        System.out.println("WebRtc closing");
        onlineUsers.remove(user);
        // String message = String
        // .format("* %s %s", nickname, "has disconnected.");
        // broadcast(message);
    }

    @OnMessage
    public void echoTextMessage(Session session, String msg) throws IOException {
//        String s = JSON.toJSONString(msg);
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String type = jsonObject.getString("type");
        if (CLIENT_USER_EVENT_LOGIN.equals(type)){
            broadcast();
        }else {
            sendToFriend(msg,jsonObject.getString("aimId"));
        }
//        switch (type){
//            case CLIENT_USER_EVENT_LOGIN:
//                broadcast();
//                break;
//            case CLIENT_RTC_EVENT:
//                  sendToFriend(msg,jsonObject.getString("aimId"));
//        }

    }

    @OnError
    public void onError(Throwable t) {
        System.out.println("Chat Error: " + t.toString());
    }

    private static void sendToFriend(String msg,String aimId) {
        for (User u : onlineUsers) {
            try {
                if (aimId.equals(u.id)) {
                    u.session.getBasicRemote().sendText(msg);
                }
            }catch (Exception e){
                log.error("发起视频聊天异常");
            }

        }
    }

    private  void broadcast() throws IOException {
        Map<String,String> map  = new HashMap<>();
        map.put("type","SERVER_USER_EVENT_UPDATE_USERS");
        map.put("value",JSON.toJSONString(getOnlineUser()));
        for (User onlineUser : onlineUsers) {
            onlineUser.getSession().getBasicRemote().sendText(JSON.toJSONString(map));
        }
    }

    public List<String> getOnlineUser(){
        return onlineUsers.stream().map(User::getId).collect(Collectors.toList());
    }

    User getHost(String aimId) {
        for (User u : onlineUsers) {
            if (u.role == 1 && u.id.equals(aimId)) {
                return u;
            }
        }
        return null;
    }

    User getGuest(String aimId) {
        for (User u : onlineUsers) {
            if (u.role == 2 && u.id.equals(aimId)) {
                return u;
            }
        }
        return null;
    }

    User getAnother(String aimId) {
        if (user.role == 1) {
            return getGuest(aimId);
        }
        if (user.role == 2) {
            return getHost(aimId);
        }
        return null;
    }
}
