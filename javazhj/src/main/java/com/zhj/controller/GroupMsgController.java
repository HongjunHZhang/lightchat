package com.zhj.controller;


import com.zhj.entity.GroupMsg;
import com.zhj.entity.ReportInfo;
import com.zhj.entity.result.DataResult;
import com.zhj.filter.ExcludeLogParam;
import com.zhj.service.IGroupMsgService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhj
 * @since 2022-02-21
 */
@RestController
@RequestMapping("/lightChat/groupMsg")
public class GroupMsgController {
    @Resource
    IGroupMsgService groupMsgService;

    @ExcludeLogParam(ignoreLog = false)
    @PostMapping("/getDefaultMsg")
    public DataResult getDefaultMsg(@RequestBody String json){
       return DataResult.successOfData(groupMsgService.getDefaultMsg(json));
    }
    @PostMapping("/getNextMsg")
    public DataResult getNextMsg(@RequestBody String json){
        return DataResult.successOfData(groupMsgService.getNextMsg(json));
    }

    @ExcludeLogParam(ignoreLog = false)
    @PostMapping("/sendMsg")
    public DataResult getDefaultMsg(@RequestParam(value = "file",required = false) MultipartFile file, @RequestParam(value = "bsId",required = false) String bsId, @RequestParam(value = "defaultInfo",required = false) String defaultInfo) throws IOException {
        if (groupMsgService.saveMsg(file, defaultInfo, bsId)){
            return DataResult.successOfData("成功");
        }
        return DataResult.errorOfData();
    }

    @PostMapping("/withdrawGroupMsg")
    public DataResult withdrawGroupMsg(@RequestBody GroupMsg groupMsg){
        return DataResult.successOfData(groupMsgService.withdrawGroupMsg(groupMsg));
    }


}
