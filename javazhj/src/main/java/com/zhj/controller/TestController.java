package com.zhj.controller;

import com.zhj.entity.result.DataResult;
import com.zhj.exception.CustomException;
import com.zhj.exception.ExceptionCodeEnum;
import com.zhj.service.impl.FriendMsgServiceImpl;
import com.zhj.util.RedisUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("/lightChat/test")
@RestController
public class TestController {
    @Resource
    RedisUtil redisUtil;
    @Resource
    FriendMsgServiceImpl friendMsgService;

    @RequestMapping("/test")
    public void  test(String name){
//        redisUtil.set("zhj","123456");
//        String str = redisUtil.getString("zhj");
        friendMsgService.modifyCount();
    }


//    @RequestMapping("/test")
//    public String testt(){
//        return "helloWord";
//    }

    @RequestMapping("/test2")
    public DataResult testAuthor(){
        throw new CustomException(ExceptionCodeEnum.DATA_BASE_CONNECTION_ERROR.getMsg());
//        return DataResult.successOfNoData();
//        Thread thread = Thread.currentThread();
//        TokenBody tokenBody = UserContext.get();
//        return null;
    }

}
