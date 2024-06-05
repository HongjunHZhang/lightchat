package com.zhj.controller;

import com.zhj.entity.UserCount;
import com.zhj.entity.login.TokenBody;
import com.zhj.entity.result.DataResult;
import com.zhj.entity.ret.UserCountVerify;
import com.zhj.entity.usercontroller.UserContext;
import com.zhj.filter.ExcludeLogParam;
import com.zhj.service.IUserCountService;
import com.zhj.util.entry.JwtUtils;
import com.zhj.util.SimpleUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 789
 */
@RestController
@RequestMapping("/lightChat/login")
public class LoginController {
   @Resource
   IUserCountService userCountService;

    @GetMapping("/getVerificationCode")
    public String getVerificationCode(){
        return SimpleUtil.getVerificationCode();
    }


   @ExcludeLogParam(ignoreLog = false)
   @PostMapping("/login")
    public DataResult login(@RequestBody UserCount userCount, HttpServletResponse httpResponse){
        return DataResult.successOfData(userCountService.login(userCount,httpResponse));
   }

    @ExcludeLogParam(ignoreLog = false)
    @PostMapping("/touristLogin")
    public DataResult touristLogin(HttpServletResponse httpResponse){
        return DataResult.successOfData(userCountService.touristLogin(httpResponse));
    }

   @PostMapping("/updateAuthorization")
    public DataResult updateAuthorization(){
       TokenBody authorization = UserContext.get();
       String jwtAuthorization = JwtUtils.getJwtAuthorization(authorization);
       return DataResult.successOfData(jwtAuthorization);
   }

    @ExcludeLogParam(ignoreLog = false)
    @PostMapping("/register")
    public DataResult register(@RequestBody UserCountVerify userCountVerify){
     return DataResult.successOfData(userCountService.register(userCountVerify));
   }

    @PostMapping("/register/emailVerifyCode")
    public DataResult emailVerifyCode(@RequestBody String json) throws Exception {
        return DataResult.successOfData(userCountService.emailVerifyCode(json));
    }

    @PostMapping("/outLine")
    public DataResult outLine(){
        userCountService.outLine();
        return DataResult.successOfData("退出成功");
    }





}
