package com.zhj.controller;


import com.zhj.entity.UserCount;
import com.zhj.entity.result.DataResult;
import com.zhj.entity.usercontroller.UserContext;
import com.zhj.filter.LimitRequest;
import com.zhj.service.IUserCountService;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhj
 * @since 2021-11-17
 */
@RestController
@RequestMapping("/lightChat/userCount")
public class UserCountController {
    @Resource
    IUserCountService iUsercountService;

    @PostMapping("/getUserInfo")
    public DataResult getUserInfo(){
        String userCount = UserContext.getUserCount();
        UserCount userCountInfo = iUsercountService.getUserInfoByUserCount(userCount);
        return DataResult.successOfData(userCountInfo);
    }

    @PostMapping("/getFriendAndGroup")
    public DataResult getFriendAndGroup(){
        return DataResult.successOfData(iUsercountService.getFriendAndGroup());
    }

    @LimitRequest
    @PostMapping("/getNewMsg")
    public DataResult getNewMsg(){
            return DataResult.successOfData(iUsercountService.getNewMsg());
    }

    @PostMapping("/loadHistoryMsg")
    public DataResult getCurrentMsg(){
        return DataResult.successOfData(iUsercountService.loadHistoryMsg());
    }

    @PostMapping("/getFriendInfo")
    public DataResult getFriendInfo(@RequestBody String json){
        return DataResult.successOfData(iUsercountService.getFriendInfo(json));
    }

//    @PostMapping("/updateUserInfo")
//    public DataResult updateUserInfo(@RequestBody UserCount userCount,HttpServletRequest request){
//        String userCountToken = JwtUtils.getUserCount(request);
//        if (!userCount.getUserCount().equals(userCountToken)){
//            return DataResult.errorOfData("操作非法");
//        }
//        UserCount userCountSelect;
//        if (!userCount.getPassword().equals("**********")){
//            userCountSelect = userCountMapper.selectOne(new QueryWrapper<UserCount>().eq("user_count", userCount.getUserCount())
//                    .eq("password",AES.encrypt(userCount.getPassword())));
//            if (userCountSelect==null){
//                return DataResult.errorOfData("密码错误,请重试");
//            }
//            String password = request.getHeader("repeatPassword");
//            userCountSelect.setPassword(AES.encrypt(password));
//            UserId userId = new UserId(userCount.getUserCount(),password);
//            userIdMapper.update(userId,new QueryWrapper<UserId>().eq("user_count",userCount.getUserCount()));
//        }else {
//            userCountSelect = userCountMapper.selectOne(new QueryWrapper<UserCount>().eq("user_count", userCountToken));
//        }
//        fillUserCount(userCountSelect,userCount);
//         userCountMapper.update(userCountSelect,new QueryWrapper<UserCount>().eq("id",userCountSelect.getId()));
//        return DataResult.successOfData("修改信息成功");
//    }
    @PostMapping("/updateUserInfo")
     public DataResult updateUserInfo(@RequestParam(value = "photo",required = false) MultipartFile photo, @RequestParam("userInfo") String json){
        return DataResult.successOfData(iUsercountService.updateUserInfo(photo,json));
    }

    @PostMapping("/updatePassword")
    public DataResult updatePassword(@RequestBody String json){
        return DataResult.successOfData(iUsercountService.updatePassword(json));
    }

    @PostMapping("/resetAllUserPassword")
    public DataResult resetAllUserPassword(HttpServletRequest httpRequest){
        return DataResult.successOfData(iUsercountService.resetAllUserPassword(httpRequest.getHeader("password")));
    }

    @PostMapping("/submitNewShip")
    public DataResult submitNewShip(@RequestParam("shipType") String shipType){
        return DataResult.successOfData(iUsercountService.submitNewShip(shipType));
    }

    @PostMapping("/updateShipType")
    public DataResult updateShipType(@RequestBody String json){
       return DataResult.successOfData(iUsercountService.updateShipType(json));
    }

    @PostMapping("/getCurrentTalkCount")
    public DataResult getCurrentTalkCount(){
       return DataResult.successOfData(iUsercountService.getCurrentTalkCount());
    }

    @PostMapping("/getSearchCountList")
    public DataResult getSearchCountList(){
        return DataResult.successOfData(iUsercountService.getSearchCountList());
    }

    @PostMapping("/forwardMsg")
    public DataResult forwardMsg(@RequestParam("forwardPeople") String forwardPeople, @RequestParam("msgValue") String msgValue){
        return DataResult.successOfData(iUsercountService.forwardMsg(forwardPeople,msgValue));
    }

}
