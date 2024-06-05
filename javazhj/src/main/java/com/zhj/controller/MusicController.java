package com.zhj.controller;

import com.zhj.entity.FileResource;
import com.zhj.entity.result.DataResult;
import com.zhj.util.FileUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * MusicController
 *
 * @author zhangHongJun
 * @description TODO * @date
 * 2022/11/11 13:43
 */
@RestController
@RequestMapping("/lightChat/music")
public class MusicController {

    @GetMapping("/getMusicResource")
    public DataResult<List<FileResource>> getMusicResource(){
         return DataResult.successOfData(FileUtil.getMusicResource());
    }


}
