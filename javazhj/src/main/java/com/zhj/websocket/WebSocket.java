package com.zhj.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhj.entity.*;
import com.zhj.entity.usercontroller.UserContext;
import com.zhj.entity.webSocket.WebSocketMsg;
import com.zhj.filter.KafkaSendResultHandler;
import com.zhj.mapper.GroupCountMapper;
import com.zhj.mapper.GroupMemberMapper;
import com.zhj.mapper.UserCountMapper;
import com.zhj.util.EmojiUtils;
import com.zhj.util.cache.CacheRoom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
@Component
@ServerEndpoint(value = "/connectWebSocket/{userId}")
public class WebSocket implements ApplicationContextAware {

    //type 0私聊消息,1群聊,2下线,3上线

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 在线人数
     */
    public static int onlineNumber = 0;
    /**
     * 以用户的姓名为key，WebSocket为对象保存起来
     */
    private static final Map<String, WebSocket> clients = new ConcurrentHashMap<String, WebSocket>();
    /**k
     * 会话
     */
    private Session session;
    /**
     * 用户名称
     */
    private String userId;
    /**
     * 建立连接
     *
     */
//    private static ApplicationContext applicationContext;
    private static ApplicationContext applicationContext;

    private static KafkaTemplate kafkaTemplate;

    private static GroupMemberMapper groupMemberMapper;

    private  static UserCountMapper userCountMapper;

    private  static GroupCountMapper groupCountMapper;

    public static final WebSocket websocket = new WebSocket();


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        WebSocket.applicationContext = applicationContext;
        kafkaTemplate = applicationContext.getBean(KafkaTemplate.class); //获取kafka的Bean实例
        kafkaTemplate.setProducerListener(applicationContext.getBean(KafkaSendResultHandler.class));
        groupMemberMapper = applicationContext.getBean(GroupMemberMapper.class);
        userCountMapper = applicationContext.getBean(UserCountMapper.class);
        groupCountMapper = applicationContext.getBean(GroupCountMapper.class);
    }

    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session)
    {
        onlineNumber++;
        logger.info("现在来连接的客户id："+session.getId()+"用户名："+userId);
        this.userId = userId;
        this.session = session;
        logger.info("有新连接加入！ 当前在线人数" + onlineNumber);

        userCountMapper.online(userId);

        try {
            //messageType 1代表上线 2代表下线 3代表在线名单 4代表普通消息
            //先给所有人发送通知，说我上线了
            HashMap<String,String> retMap = new HashMap<>();
            retMap.put("type","3");
            retMap.put("userId",userId);
            sendMessageAll(JSON.toJSONString(retMap),userId);

            //把自己的信息加入到map当中去
            clients.put(userId, this);
            List<GroupMember> groupList = groupMemberMapper.selectList(new QueryWrapper<GroupMember>().eq("user_count", userId));
            for (GroupMember group : groupList) {
               WebSocketRoom.addMemberOfRoom(this,group.getGroupCount(),userId);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            logger.info(userId+"上线的时候通知所有人发生了错误");
        }



    }

    @OnError
    public void onError(Session session, Throwable error) {
        logger.info("服务端发生了错误"+error.getMessage());
        //error.printStackTrace();
    }
    /**
     * 连接关闭
     */
    @OnClose
    public void onClose()
    {
        onlineNumber--;

        removeOutLineUser(userId);
        userCountMapper.outline(userId);
        clients.remove(userId);
        try {
            //messageType 1代表上线 2代表下线 3代表在线名单  4代表普通消息
            Map<String,Object> retMap = new HashMap<String,Object>();
            retMap.put("type","2");
            retMap.put("onlineUsers",clients.keySet());
            retMap.put("aimId","all");
            retMap.put("userId",userId);
            sendMessageAll(JSON.toJSONString(retMap),userId);
        }
        catch (IOException e){
            logger.info(userId+"下线的时候通知所有人发生了错误");
            System.out.println("有错误发生");
        }
        //logger.info("有连接关闭！ 当前在线人数" + onlineNumber);
          logger.info("有连接关闭！ 当前在线人数" + clients.size());
    }


    /**
     * 收到客户端的消息
     *
     * @param message 消息
     * @param session 会话
     */
    @OnMessage
    public void onMessage(String message, Session session)
    {
        logger.info("来自客户端消息：" + message+"客户端的id是："+session.getId());
        sendMsgAfterReceive(JSON.parseObject(message,WebSocketMsg.class));
    }

    public static void htmlReceiveMsg(Object msg, String bsId){
        WebSocketMsg webSocketMsg = new WebSocketMsg();
        if (msg instanceof FriendMsg){
            FriendMsg friendMsg = (FriendMsg) msg;
            webSocketMsg.setId(friendMsg.getId());
            webSocketMsg.setMsg(friendMsg.getMsg());
            webSocketMsg.setSendId(friendMsg.getSendId());
            webSocketMsg.setReceiveId(friendMsg.getReceiveId());
            webSocketMsg.setMsgType("0");
            webSocketMsg.setCountPhoto(friendMsg.getCountPhoto());
            webSocketMsg.setSendNickName(friendMsg.getSendNickName());
            webSocketMsg.setFileName(friendMsg.getFileName());
            webSocketMsg.setType(friendMsg.getType());
//            webSocketMsg.setGroupId(webSocketMsg.getGroupId());
//            webSocketMsg.setGroupPhoto(webSocketMsg.getCountPhoto());
//            webSocketMsg.setGroupNickName(webSocketMsg.getSendNickName());
        }
        webSocketMsg.setBsId(bsId);

        if (msg instanceof GroupMsg){
            UserCount userCount = CacheRoom.getUserCountOfCount(UserContext.getUserCount());
            GroupMsg groupMsg = (GroupMsg) msg;
            webSocketMsg.setId(groupMsg.getId());
            webSocketMsg.setMsg(groupMsg.getMsg());
            webSocketMsg.setSendId(groupMsg.getSendId());
            webSocketMsg.setReceiveId("all");
            webSocketMsg.setMsgType("1");
            webSocketMsg.setCountPhoto(userCount.getPhoto());
            webSocketMsg.setSendNickName(userCount.getNickName());
            webSocketMsg.setFileName(groupMsg.getFileName());
            webSocketMsg.setType(groupMsg.getType());
            webSocketMsg.setGroupId(groupMsg.getGroupId());
            webSocketMsg.setGroupPhoto(groupMsg.getGroupPhoto() == null ? groupMsg.getCountPhoto() : groupMsg.getGroupPhoto());
            webSocketMsg.setGroupNickName(groupMsg.getGroupName());
        }
        websocket.sendMsgAfterReceive(webSocketMsg);
    }

    public void  sendMsgAfterReceive(WebSocketMsg webSocketMsg){
        if (sendByLocal(webSocketMsg)){
            receiveMsg(webSocketMsg);
        }else {
            try {
                kafkaTemplate.send("lcMsg", JSON.toJSONString(webSocketMsg));
            }catch (Exception e){

            }
        }
    }


    public void receiveMsg(WebSocketMsg webSocketMsg){
        try {
            //如果不是发给所有，那么就发给某一个人
            //messageType 1代表上线 2代表下线 3代表在线名单  4代表普通消息
//          Messagetype messagetype=new Messagetype(message,session.getId(),"所有人","4");
//            Map<String,Object> retMap = new HashMap<>();
//            retMap.put("msg",webSocketMsg.getMsg());
//            retMap.put("id",webSocketMsg.getId());
//            retMap.put("sendId",webSocketMsg.getSendId());
//            retMap.put("msgType", webSocketMsg.getMsgType());
//            retMap.put("countPhoto",webSocketMsg.getCountPhoto());
//            retMap.put("sendNickName",webSocketMsg.getSendNickName());
//            retMap.put("bsId",map.get("bsId"));
//            retMap.put("fileName",map.get("fileName"));
//            retMap.put("groupId",map.getOrDefault("groupId","0"));
//            retMap.put("groupPhoto",map.getOrDefault("groupPhoto","0"));
//            retMap.put("groupNickName",map.getOrDefault("groupNickName","0"));
            webSocketMsg.setMsg(EmojiUtils.emojiRecovery2(webSocketMsg.getMsg()));
            if(("1".equals(webSocketMsg.getMsgType()))) {   //群聊的消息发送方法
                webSocketMsg.setReceiveId("所有人");
                String roomId = webSocketMsg.getGroupId();
                sendMessageOfRoom(JSON.toJSONString(webSocketMsg),roomId);
            }else if("0".equals(webSocketMsg.getMsgType())){
                sendMessageTo(JSON.toJSONString(webSocketMsg),webSocketMsg.getSendId());
                sendMessageTo(JSON.toJSONString(webSocketMsg),webSocketMsg.getReceiveId());
            }
        }
        catch (Exception e){
            e.printStackTrace();
            logger.info("发生了错误了");
        }

    }

    public void kafkaReceiveMsg(String message) {
        WebSocketMsg webSocketMsg = JSON.parseObject(message, WebSocketMsg.class);
        //接受者ID
        String receiverId = webSocketMsg.getReceiveId();
        String userId = webSocketMsg.getSendId();
        String type = webSocketMsg.getMsgType();
        if ("1".equals(type) || clients.get(userId) != null || clients.get(receiverId) != null ) {
            receiveMsg(webSocketMsg);
        }
    }

    public void sendMessageTo(String message, String aimId) throws IOException {
        WebSocket webSocket = clients.get(aimId);
       if (webSocket == null){
           return;
       }
       synchronized (webSocket.session){
           webSocket.session.getAsyncRemote().sendText(message);
       }

    }

    public void sendMessageAll(String message,String userId) throws IOException {
        for (WebSocket item : clients.values()) {
            item.session.getAsyncRemote().sendText(message);
        }
    }
    public void sendMessageOfRoom(String message,String roomId) throws IOException {
        Map<String, WebSocket> roomMap = WebSocketRoom.getRooms().get(roomId);
        if (roomMap == null){
            return;
        }
        for (WebSocket item : roomMap.values()) {
            item.session.getAsyncRemote().sendText(message);
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineNumber;
    }

    public static synchronized Map<String, WebSocket> getClients(){
        return clients;
    }

    public  String getUserId(){
        return this.userId;
    }

    public void removeOutLineUser(String userId){
            List<GroupMember> groupList = groupMemberMapper.selectList(new QueryWrapper<GroupMember>().eq("user_count", userId));
            for (GroupMember group : groupList) {
                WebSocketRoom.deleteMemberOfRoom(group.getGroupCount(),userId);
            }
    }

    public boolean sendByLocal(WebSocketMsg webSocketMsg){
        return KafkaSendResultHandler.kafkaNotWork() || (isPrivateTalk(webSocketMsg) && clients.containsKey(webSocketMsg.getReceiveId()));
    }

    public boolean isPrivateTalk(WebSocketMsg webSocketMsg){
        return "0".equals(webSocketMsg.getType());
    }

}
