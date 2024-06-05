package com.zhj.controller;


import com.zhj.entity.SpaceRemark;
import com.zhj.entity.SpaceValue;
import com.zhj.entity.result.DataResult;
import com.zhj.filter.LimitRequest;
import com.zhj.service.ISpaceCountService;
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
 * @since 2022-03-09
 */
@RestController
@RequestMapping("/lightChat/spaceCount")
public class SpaceCountController {

    @Resource
    ISpaceCountService spaceCountService;


    @LimitRequest(times = 1,durationTime = 10)
    @PostMapping("/talk")
    public DataResult talk(@RequestParam(value = "files",required = false) MultipartFile[] file, @RequestParam("msg") String msg, HttpServletRequest request) throws IOException {
       return DataResult.successOfData(spaceCountService.talk(file,msg));
    }

    @LimitRequest(times = 1,durationTime = 3)
    @PostMapping("/deleteTalk")
    public DataResult deleteTalk(@RequestParam("talkId") Integer talkId,HttpServletRequest request) throws IOException {
        return DataResult.successOfData(spaceCountService.deleteTalk(talkId));
    }


    @LimitRequest(durationTime = 1)
    @PostMapping("/getDefaultInfo")
    public DataResult getDefaultInfo(@RequestBody String json) {
        return DataResult.successOfData(spaceCountService.getDefaultInfo(json));
    }

    @PostMapping("/goBackSpace")
    public DataResult goBackSpace(@RequestBody String json) {
        return DataResult.successOfData(spaceCountService.goBackSpace(json));
    }

    @PostMapping("/getSpaceInfo")
    public DataResult getSpaceInfo(@RequestBody String json) {
        return DataResult.successOfData(spaceCountService.getSpaceInfo(json));
    }

    @PostMapping("/getRandomSpace")
    public DataResult getRandomSpace() {
        return DataResult.successOfData(spaceCountService.getRandomSpace());
    }

    @PostMapping("/getFriendInfo")
    public DataResult getFriendInfo(@RequestBody String json) {
        return DataResult.successOfData(spaceCountService.getFriendInfo(json));
    }

    @PostMapping("/getSpaceInfoByUser")
    public DataResult getSpaceInfoByUser(@RequestBody String json) {
        return DataResult.successOfData(spaceCountService.getSpaceInfoByUser(json));
    }


    @PostMapping("/getCurrentTalk")
    public DataResult getCurrentTalk() {
        return DataResult.successOfData(spaceCountService.getCurrentTalk());
    }


    @PostMapping("/getCurrentVisitors")
    public DataResult getCurrentVisitors() {
        return DataResult.successOfData(spaceCountService.getCurrentVisitors());
    }

    @PostMapping("/submitRemark")
    public DataResult submitRemark(@RequestBody SpaceRemark spaceRemark) {
        return DataResult.successOfData(spaceCountService.submitRemark(spaceRemark));
    }
    @PostMapping("/thumb")
    public DataResult thumb(@RequestBody SpaceValue spaceValue) {
        return DataResult.successOfData(spaceCountService.thumb(spaceValue));
    }

    @PostMapping("/autoSpace")
    public DataResult autoSpace() {
       return DataResult.successOfData(spaceCountService.autoSpace());
    }

    @GetMapping("/autoFillFriend")
    public DataResult autoFillFriend() {
        spaceCountService.autoFillFriend();
        return DataResult.successOfNoData();
    }

    @PostMapping("/submitPrivateTalk")
    public DataResult submitPrivateTalk(@RequestBody String json) {
        return DataResult.successOfData(spaceCountService.submitPrivateTalk(json));
    }

    @PostMapping("/getPrivateTalk")
    public DataResult getPrivateTalk(@RequestBody String json) {
       return DataResult.successOfData(spaceCountService.getPrivateTalk(json));
    }

    @PostMapping("/getSpacePhotoList")
    public DataResult getSpacePhotoList(@RequestBody String json) {
       return DataResult.successOfData(spaceCountService.getSpacePhotoList(json));
    }




}
