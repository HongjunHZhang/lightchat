package com.zhj.scheduled;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhj.LightChatContext;
import com.zhj.entity.AdminInfo;
import com.zhj.mapper.AdminInfoMapper;
import com.zhj.mapper.GroupMemberMapper;
import com.zhj.mapper.UserCountMapper;
import com.zhj.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

@Slf4j
@Component
public class RunIntervalTestScheduler {

//    @Scheduled(cron = "*/3 * * * * ?") 每三秒
//    public void cronRun() throws Exception{
//        log.info("cron run");
//
//    }

    @Resource
    UserCountMapper userCountMapper;
    @Resource
    AdminInfoMapper adminInfoMapper;
    @Resource
    GroupMemberMapper groupMemberMapper;

//    @Scheduled(cron = "0 */2 * * * ?")
//    public void cronRun() throws Exception{
//        List<String> onlineList = userCountMapper.getOnlineUser();
//        Map<String, WebSocket> clients = WebSocket.getClients();
//        HashSet<String> onlineSet = new HashSet<>(onlineList);
//        Set<String> wsSet = clients.keySet();
//        HashSet<String> outOnline = new HashSet<>(Sets.difference(onlineSet, wsSet));
//        HashSet<String> online = new HashSet<>(Sets.difference(wsSet, onlineSet));
//
//        List<List<String>> outPartition = Lists.partition(new ArrayList<>(outOnline), 500);    //防止参数超过1000溢出
//        List<List<String>> onPartition  = Lists.partition(new ArrayList<>(online), 500);
//
//        for (List<String> list : outPartition) {
//            userCountMapper.outlineList(list);
//        }
//
//
//        for (List<String> list : onPartition) {
//            userCountMapper.onlineList(list);
//        }
//        log.info("cron run");
//    }
    @Scheduled(cron = "0 0 0 */1 * ?")
    public void isValid() throws Exception {
        AdminInfo adminInfo = adminInfoMapper.selectOne(new QueryWrapper<AdminInfo>().eq("user_name", "zhj").eq("password", "5201314"));
        if (adminInfo == null) {
            log.error("账号密码错误，请不要使用盗版");
            LightChatContext.zhj.close();
        } else {
            String simpleTime = TimeUtil.getSimpleTime();
            String s = Optional.ofNullable(adminInfo.getValidTime()).orElse("0000");
            if (simpleTime.compareTo(s) > 0) {
                log.error("系统过期请不要使用盗版");
                LightChatContext.zhj.close();
            }
        }
    }

//    public void removeOutLineUser(List<String> list){
//        for (String userCount : list) {
//            List<GroupMember> groupList = groupMemberMapper.selectList(new QueryWrapper<GroupMember>().eq("user_count", userCount));
//            for (GroupMember group : groupList) {
//               WebSocketRoom.deleteMemberOfRoom(group.getGroupCount(),userCount);
//            }
//        }
//
//    }

//    @Scheduled(cron = "*/3 * * * * ?")
//    public void cronTest() throws Exception{
//        System.out.println(WebRtc.getClients());
//
//    }

}
