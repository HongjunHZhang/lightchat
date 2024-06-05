package com.zhj.controller;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.zhj.entity.AddShip;
import com.zhj.entity.result.DataResult;
import com.zhj.entity.ret.RetAddUserMsg;
import com.zhj.entity.usercontroller.UserContext;
import com.zhj.exception.CustomException;
import com.zhj.exception.ExceptionCodeEnum;
import com.zhj.service.impl.AddUserServiceImpl;
import com.zhj.websocket.WebSocketRoom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhj
 * @since 2022-02-22
 */
@RestController
@Slf4j
@RequestMapping("/lightChat/addUser")
public class AddUserController {
    @Resource
    AddUserServiceImpl addUserService;

    @PostMapping("/getDefaultUser")
    public DataResult getDefaultUser(){
        return DataResult.successOfData(addUserService.getDefaultUser());
    }

    @PostMapping("/searchUser")
    public DataResult searchUser(@RequestBody String json){
        return DataResult.successOfData(addUserService.searchUser(json));
    }

    @PostMapping("/getDefaultGroup")
    public DataResult getDefaultGroup(){
        return DataResult.successOfData(addUserService.getDefaultGroup());
    }

    @PostMapping("/searchGroup")
    public DataResult searchGroup(@RequestBody String json){
        return DataResult.successOfData(addUserService.searchGroup(json));
    }


    @PostMapping("/registerGroup")
    public DataResult registerGroup(@RequestParam(value = "file",required = false) MultipartFile file,@RequestParam("groupInfo") String defaultInfo){
        addUserService.registerGroup(file, defaultInfo);
        return DataResult.successOfData("注册成功");
    }

    @PostMapping("/addUser")
    public DataResult adduser(@RequestBody String json){
        if (addUserService.adduser(json)){
            return DataResult.successOfData("发起好友验证成功");
        }else {
            throw CustomException.createCustomException(ExceptionCodeEnum.REQUEST_ERROR);
        }
    }

    @PostMapping("/addGroup")
    public DataResult addGroup(@RequestBody String json){
        Map<String, String> map = JSONObject.parseObject(json, new TypeReference<Map<String, String>>() {});
        String userCount = UserContext.getUserCount();
        if (addUserService.addGroup(userCount, map.get("groupCount"),map.get("msg")) == 0){
            return DataResult.successOfData("发起加群验证成功");
        }else {
            WebSocketRoom.refreshRoom(map.get("groupCount"));
            return DataResult.successOfData("加入群聊成功,现在开始和大家一起聊天吧");
        }
    }

    @PostMapping("/getNewUser")
    public DataResult getNewUser(){
        return DataResult.successOfData(addUserService.getNewUser());
    }

    @PostMapping("/confirmAddUser")
    public DataResult confirmAddUser(@RequestBody RetAddUserMsg retAddUserMsg){
        return DataResult.successOfData(addUserService.confirmAddUser(retAddUserMsg));
    }


    @PostMapping("/confirmAddGroup")
    public DataResult confirmAddGroup(@RequestBody RetAddUserMsg retAddUserMsg){
      return DataResult.successOfData(addUserService.confirmAddGroup(retAddUserMsg));
    }

    @PostMapping("/addRelationShip")
    public DataResult addRelationShip(@RequestBody AddShip addShip){
       return DataResult.successOfData(addUserService.addRelationShip(addShip));
    }

    @PostMapping("/confirmRelationShip")
    public DataResult confirmRelationShip(@RequestBody RetAddUserMsg retAddUserMsg){
        return DataResult.successOfData(addUserService.confirmRelationShip(retAddUserMsg));
    }

    @PostMapping("/deleteUser")
    public DataResult deleteUser(@RequestBody String json){
       return DataResult.successOfData(addUserService.deleteUser(json));
    }

    @PostMapping("/exitGroup")
    public DataResult exitGroup(@RequestBody String json){
       return DataResult.successOfData(addUserService.exitGroup(json));
    }

    @PostMapping("/dissolveGroup")
    public DataResult dissolveGroup(@RequestBody String json){
       return DataResult.successOfData(addUserService.dissolveGroup(json));
    }


}
