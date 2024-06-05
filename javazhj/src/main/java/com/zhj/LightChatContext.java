package com.zhj;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zhj.config.AdminConfig;
import com.zhj.entity.UserCount;
import com.zhj.service.impl.UserCountServiceImpl;
import com.zhj.util.entry.Md5Util;
import com.zhj.websocket.WebSocketRoom;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

/**
 * @author 789
 */
@SpringBootApplication
@EnableScheduling
public class LightChatContext {
   public static ConfigurableApplicationContext zhj;

    public static void main(String[] args) {
       zhj =  SpringApplication.run(LightChatContext.class, args);
       init(zhj);
    }

    static void init(ConfigurableApplicationContext zhj){
        booleanValid(zhj);
        new WebSocketRoom().setRoom(zhj);
    }

   static void booleanValid(ConfigurableApplicationContext zhj){
        if (!AdminConfig.isValid){
            zhj.close();
        }

//       UserCountServiceImpl userCountService = zhj.getBean(UserCountServiceImpl.class);
//       List<UserCount> list = userCountService.list(new QueryWrapper<UserCount>());
//       list.forEach(t->{
//           t.setPassword(Md5Util.createMd5ByPassword("12345678"));
//           userCountService.update(t,new UpdateWrapper<UserCount>().lambda().eq(UserCount::getId,t.getId()));
//       });

   }

}



































