package com.zhj.websocket;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhj.entity.GroupCount;
import com.zhj.entity.GroupMember;
import com.zhj.mapper.GroupCountMapper;
import com.zhj.mapper.GroupMemberMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 789
 */
@Component
public class WebSocketRoom {

    private static final Map<String, Map<String,WebSocket>> rooms = new ConcurrentHashMap<>();
    public static GroupMemberMapper groupMemberMapper;
    private static ApplicationContext context;
    /**
     *
     * 一个初始化房间的方法，可以初始化所有群组并放入内存
     */
    public  void setRoom(ApplicationContext applicationContext){
        if (context == null){
            context = applicationContext;
        }
        GroupCountMapper groupCountMapper = applicationContext.getBean(GroupCountMapper.class);
        List<GroupCount> groupCounts = groupCountMapper.selectList(new QueryWrapper<>());
        for (GroupCount groupCount : groupCounts) {
            rooms.put(groupCount.getGroupCount(),new HashMap<>());
        }

    }

    /**
     *一个增加房间成员的方法
     */
    public static void addMemberOfRoom(WebSocket webSocket,String roomId,String userId){
        Map<String,WebSocket> map = rooms.get(roomId);
        if (map == null){
            return;
        }
        map.put(userId,webSocket);
    }

    /**
     *一个移除房间成员的方法
     */
    public static void deleteMemberOfRoom(String roomId,String userId){
        Map<String,WebSocket> map = rooms.get(roomId);
        if (map == null){
            return;
        }
        map.remove(userId);
    }

    public static void addRoom(String roomId,HashMap<String,WebSocket> map){
        rooms.put(roomId,map);
        refreshRoom(roomId);
    }

    /**
     *
     * 手动向房间添加可能会新增的新成员
     */
    public static void refreshRoom(String roomId){
        if (groupMemberMapper == null){
            groupMemberMapper = context.getBean(GroupMemberMapper.class);
        }
        Map<String, WebSocket> roomMap = rooms.get(roomId);
        List<GroupMember> userList = groupMemberMapper.selectList(new QueryWrapper<GroupMember>().eq("group_count", roomId));
        for (GroupMember groupMember : userList) {
            Map<String, WebSocket> clients = WebSocket.getClients();
            WebSocket webSocket = clients.get(groupMember.getUserCount());
            if (webSocket != null && !roomMap.containsKey(webSocket.getUserId())){
                    roomMap.put(webSocket.getUserId(),webSocket);
           }
        }
    }

    public static synchronized Map<String, Map<String,WebSocket>> getRooms(){
        return rooms;
    }

}
