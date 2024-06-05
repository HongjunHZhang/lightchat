package com.zhj.controller;

import com.zhj.entity.result.DataResult;
import com.zhj.filter.ExcludeLogParam;
import com.zhj.service.IFriendMsgService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author 789
 */
@RestController
@RequestMapping("/lightChat/friendMsg")
public class FriendMsgController {

    @Resource
    IFriendMsgService friendMsgService;

    @ExcludeLogParam(ignoreLog = false)
    @PostMapping("/getDefaultMsg")
    public DataResult getDefaultMsg(@RequestBody String json){
      return friendMsgService.getDefaultMsg(json);
    }

    @ExcludeLogParam(ignoreLog = false)
    @PostMapping("/sendMsg")
    public DataResult sendMsg(@RequestParam(value = "file",required = false) MultipartFile file,@RequestParam(value = "defaultInfo",required = false) String defaultInfo,
        @RequestParam(value = "viaGroup",required = false) String viaGroup,@RequestParam(value = "bsId",required = false) String bsId) throws IOException {
        if (friendMsgService.sendMsg(file, defaultInfo, viaGroup, bsId)){
            return  DataResult.successOfData("成功");
        }
        return DataResult.errorOfData();
    }

    @PostMapping("/refuseCall")
    public DataResult refuseCall() {
        friendMsgService.refuseCall();
        return DataResult.successOfData("成功");
    }




}
