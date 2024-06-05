package com.zhj.controller;

import com.zhj.entity.result.DataResult;
import com.zhj.service.IRetrievePasswordService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author 789
 */
@RestController
@RequestMapping("/lightChat/retrievePassword")
public class RetrievePasswordController {
    @Resource
    IRetrievePasswordService retrievePasswordService;

    @PostMapping("/getVerifyCode")
    public DataResult getVerifyCode(@RequestParam("userCount") String userCount) throws Exception {
       return DataResult.successOfData(retrievePasswordService.getVerifyCode(userCount));
    }

    @PostMapping("/changePassword")
    public DataResult changePassword(@RequestBody String json)  {
       return DataResult.successOfData(retrievePasswordService.changePassword(json));
    }

    @PostMapping("/emailVerifyCode")
    public DataResult emailVerifyCode(@RequestParam("email") String email) throws Exception {
       return DataResult.successOfData(retrievePasswordService.emailVerifyCode(email));
    }

    @PostMapping("/changeEmail")
    public DataResult changeEmail(@RequestBody String json) throws Exception {
       return DataResult.successOfData(retrievePasswordService.changeEmail(json));
    }




}
