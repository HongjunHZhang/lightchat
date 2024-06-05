package com.zhj.webrtc;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhj.entity.RefuseCall;
import com.zhj.mapper.RefuseCallMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint(value = "/webRtc/{userId}")
@Slf4j
@Component
public class WebRtc {
   private static RefuseCallMapper refuseCallMapper;
   @Resource
   public  void setRefuseCallMapper(RefuseCallMapper refuseCallMapper){
    WebRtc.refuseCallMapper = refuseCallMapper;
   }

    private static final String GUEST_PREFIX = "Guest";
    private static final AtomicInteger connectionIds = new AtomicInteger(0);
    private static final Set<User> onlineUsers = new CopyOnWriteArraySet<User>();

    private final User user;

    public WebRtc() {
        user = new User();
//        user.id = connectionIds.getAndIncrement();
        user.name = GUEST_PREFIX + user.id;
        user.candidates = new ArrayList<String>();
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session) {
        user.id = userId;
        user.session = session;
        try {
            System.out.println(user.id + " joined!");
            user.session.getBasicRemote().sendText(String.format("Welcome, %s,%s!", user.name, user.session.getId()));
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
    public void echoTextMessage(Session session, String msg) {
        try {
            if (session.isOpen()) {
                JSONObject json = JSON.parseObject(msg);
                String aimId = json.getString("aimId");
                RefuseCall userSelect = refuseCallMapper.selectOne(new QueryWrapper<RefuseCall>().eq("refuse_user_id", aimId));
                if (userSelect != null){
                 return;
                }
                if (json.containsKey("makeHost")) {
                    user.role = 1;
                    onlineUsers.add(user);
                    System.out.println("makeHost=" + user.candidates.size());
                    sendToFriend(user.id + " as Host Joined!",aimId);
//                    broadcast(user.name + " as Host Joined!");
                }
                if (json.containsKey("makeGuest")) {
//                    if (onlineUsers.size() >= 2) {
//                        session.getBasicRemote().sendText("Too many person!");
//                    }
                    user.role = 2;
                    onlineUsers.add(user);
                    System.out.println("makeGuest=" + user.candidates.size());
                    sendToFriend(user.id + " as Host Joined!",aimId);
//                    broadcast(user.name + " as guest Joined!");
                }
                if (json.containsKey("sdp")) {
                    user.sessionDescription = json.get("sdp").toString();
                    System.out.println("SDP" + user.sessionDescription);
                }
                if (json.containsKey("candidate")) {
                    user.candidates.add(json.get("candidate").toString());
                    System.out.println("Candidates=" + user.candidates.size() + json.get("candidate").toString());
                }
                User another = getAnother(aimId);
                // send another msg to me
                if (another != null) {
                    if (another.sessionDescription != null && !another.sessionDescription.equals("")) {
                        JSONObject sdpJson = new JSONObject();
                        sdpJson.put("sdp", another.sessionDescription);
                        session.getBasicRemote().sendText(sdpJson.toString());
                        another.sessionDescription = "";
                    }

                    for (String s : another.candidates) {
                        JSONObject tmpJson = new JSONObject();
                        tmpJson.put("candidate", s);
                        session.getBasicRemote().sendText(tmpJson.toString());
                    }
                    another.candidates.clear();
                }
                // send my msg to another
                if (another != null) {
                    if (user.sessionDescription != null && !user.sessionDescription.equals("")) {
                        JSONObject sdpJson = new JSONObject();
                        sdpJson.put("sdp", user.sessionDescription);
                        another.session.getBasicRemote().sendText(sdpJson.toString());
                        user.sessionDescription = "";
                    }

                    for (String s : user.candidates) {
                        JSONObject tmpJson = new JSONObject();
                        tmpJson.put("candidate", s);
                        another.session.getBasicRemote().sendText(tmpJson.toString());
                    }
                    user.candidates.clear();
                }
            }
        } catch (Exception e) {
            try {
                System.out.println("Err on MSG," + e);
                session.close();
            } catch (IOException e1) {
                e.printStackTrace();
            }
        }
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

    private static void broadcast(String msg) {
        for (User u : onlineUsers) {
            try {
                synchronized (u) {
                    u.session.getBasicRemote().sendText(msg);
                }
            } catch (IOException e) {
                System.out.println("Chat Error: Failed to send message to client" + e);
                onlineUsers.remove(u);
                try {
                    u.session.close();
                } catch (IOException e1) {
                    e.printStackTrace();
                }
                String message = String.format("* %s %s", u.name,
                        "has been disconnected.");
                broadcast(message);
            }
        }
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
