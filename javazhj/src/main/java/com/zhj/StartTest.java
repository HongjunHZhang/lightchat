//package com.zhj;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
//import com.zhj.entity.UserCount;
//import com.zhj.mapper.UserCountMapper;
//import com.zhj.service.impl.FriendMsgServiceImpl;
//import com.zhj.service.impl.UserCountServiceImpl;
//import com.zhj.util.entry.Md5Util;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.annotation.Resource;
//import java.util.List;
//
///**
// * StartTest
// *
// * @author zhangHongJun
// * @description TODO * @date
// * 2023/2/14 22:13
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {LightChatContext.class})
//public class StartTest {
//    @Resource
//    UserCountServiceImpl userCountService;
//
//    @Test
//    public void test(){
//        List<UserCount> list = userCountService.list(new QueryWrapper<UserCount>());
//        list.forEach(t->{
//            t.setPassword(Md5Util.parsePasswordToMd5FromJwt(t.getPassword()));
//            userCountService.update(new UpdateWrapper<UserCount>().lambda().eq(UserCount::getId,t.getId()));
//        });
//    }
//
//
//}
