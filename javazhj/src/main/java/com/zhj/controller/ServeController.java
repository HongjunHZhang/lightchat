package com.zhj.controller;

import com.zhj.entity.result.DataResult;
import com.zhj.exception.CustomException;
import com.zhj.exception.ExceptionCodeEnum;
import com.zhj.util.FileUtil;
import com.zhj.util.cache.CacheRoom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author 789
 */
@RestController
@Slf4j
@RequestMapping("/lightChat/serve")
public class ServeController {

    @RequestMapping("/addFileByName")
    public String addFileByName( @RequestParam(value = "file",required = false) MultipartFile file,HttpServletRequest request){
         return  FileUtil.addFileByName(file,request);
    }

    @RequestMapping("/deleteFileByName")
    public DataResult deleteFileByName(HttpServletRequest request){
        if (FileUtil.deleteFileOfServer(request)){
            return DataResult.successOfData("执行成功");
        }

        return DataResult.errorOfData("失败");
    }

    @RequestMapping("/codeServe")
    public DataResult codeServe(@RequestParam(value = "file",required = false) MultipartFile file,HttpServletRequest request) throws Exception {
        if (!CacheRoom.checkAdminRole()){
            throw CustomException.createCustomException(ExceptionCodeEnum.ADMIN_ROLE_ERROR);
        }
        FileUtil.codeFile(file,request.getHeader("fileType"));
        return DataResult.errorOfData("失败");
    }

    @RequestMapping("/submitCode")
    public DataResult submitCode(@RequestParam(value = "file",required = false) MultipartFile file,@RequestParam(value = "fileType") String fileType) throws Exception {
       if (!CacheRoom.checkAdminRole()){
           throw CustomException.createCustomException(ExceptionCodeEnum.ADMIN_ROLE_ERROR);
       }
       FileUtil.submitCodeFile(file,fileType);
        return DataResult.errorOfData("失败");
    }

//    @RequestMapping("/submitVue")
//    public DataResult submitVue(@RequestParam(value = "file",required = false) MultipartFile file,@RequestParam(value = "type") String type) throws Exception {
//       if (!CacheRoom.checkAdminRole()){
//           throw CustomException.createCustomException(ExceptionCodeEnum.ADMIN_ROLE_ERROR);
//       }
//       FileUtil.submitCodeFile(file,type);
//        return DataResult.errorOfData("失败");
//    }


}
