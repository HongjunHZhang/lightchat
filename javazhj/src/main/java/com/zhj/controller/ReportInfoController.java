package com.zhj.controller;


import com.zhj.entity.ReportInfo;
import com.zhj.entity.result.DataResult;
import com.zhj.service.IReportService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhj
 * @since 2022-04-12
 */
@RestController
@RequestMapping("/lightChat/reportInfo")
public class ReportInfoController {
   @Resource
    IReportService reportService;

    @PostMapping("/reportUser")
    public DataResult reportUser(@RequestBody ReportInfo reportInfo){
        return DataResult.successOfData(reportService.reportUser(reportInfo));
     }

    @PostMapping("/getReportUser")
    public DataResult getReportUser(){
       return DataResult.successOfData(reportService.getReportUser());
    }

    @PostMapping("/banUser")
    public DataResult banUser(@RequestBody String json){
      return DataResult.successOfData(reportService.banUser(json));
    }
    @PostMapping("/getBanUser")
    public DataResult getBanUser(){
       return DataResult.successOfData(reportService.getBanUser());
    }

    @PostMapping("/unBanUser")
    public DataResult unBanUser(@RequestParam("id") Integer id){
        return DataResult.successOfData(reportService.unBanUser(id));
    }




}
