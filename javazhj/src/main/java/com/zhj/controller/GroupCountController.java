package com.zhj.controller;


import com.zhj.entity.Journal;
import com.zhj.entity.result.DataResult;
import com.zhj.service.impl.GroupCountServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhj
 * @since 2022-02-21
 */
@RestController
@RequestMapping("/lightChat/groupCount")
public class GroupCountController {
    @Resource
    GroupCountServiceImpl groupCountService;

    @PostMapping("/updateLevel")
    public DataResult updateLevel(@RequestBody String json){
       return DataResult.successOfData(groupCountService.updateLevel(json));
    }


    @PostMapping("/deleteUser")
    public DataResult deleteUser(@RequestBody String json){
       return DataResult.successOfData(groupCountService.deleteUser(json));
    }

    @PostMapping("/getGroupInfo")
    public DataResult getGroupInfo(@RequestBody String json){
       return DataResult.successOfData(groupCountService.getGroupInfo(json));
    }

    @PostMapping("/getGroupPhoto")
    public DataResult getGroupPhoto(@RequestParam("groupCount") String groupCount){
        return DataResult.successOfData(groupCountService.getGroupPhoto(groupCount));
    }

    @PostMapping("/getGroupFile")
    public DataResult getGroupFile(@RequestParam("groupCount") String groupCount){
       return DataResult.successOfData(groupCountService.getGroupFile(groupCount));
    }

    @PostMapping("/updateJournal")
    public DataResult updateJournal(@RequestBody Journal journal){
       return DataResult.successOfData(groupCountService.updateJournal(journal));
    }

    @PostMapping("/getJournal")
    public DataResult getJournal(@RequestBody String json){
        return DataResult.successOfData(groupCountService.getJournal(json));
    }

    @PostMapping("/getCurrentJournal")
    public DataResult getCurrentJournal(@RequestBody String json){
       return DataResult.successOfData(groupCountService.getCurrentJournal(json));
    }

    @PostMapping("/getJournalByDay")
    public DataResult getJournalByDay(@RequestBody String json){
        return DataResult.successOfData(groupCountService.getJournalByDay(json));
    }

    @PostMapping("/getGroupActive")
    public DataResult getGroupActive(@RequestBody String json){
        return DataResult.successOfData(groupCountService.getGroupActive(json));
    }



}
