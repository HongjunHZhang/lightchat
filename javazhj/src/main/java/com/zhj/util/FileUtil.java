package com.zhj.util;

import com.alibaba.fastjson.JSON;
import com.zhj.LightChatContext;
import com.zhj.entity.FileResource;
import com.zhj.entity.dic.DefaultSrc;
import com.zhj.entity.usercontroller.UserContext;
import com.zhj.exception.CustomException;
import com.zhj.exception.ExceptionCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.FileUtils;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 789
 */
@Slf4j
public class FileUtil {
    public static   String fileSuffix = "/home/zhj/zip";
    public static    String javaPath = "/home/zhj/java";
    public static    String vuePath = "/home/zhj/vue";
    public static    String zipFile = "/home/zhj/zip/dist";

    public static String saveFile(MultipartFile file,String fileName, String path) throws IOException {
         if (!checkServeIsLocal()){
             Map<String,String> map = new HashMap<>();
             map.put("fileName",fileName);
             map.put("path", path);
             map.put("authorization",UserContext.getAuthorization());
             return  HttpUtil.sendPostWithFile(file.getInputStream(),DefaultSrc.getServeAddress() + "addFileByName",map);
         }else{
            return saveFileToLocal(file,fileName,path);
         }
    }

    public static String saveFileAndCompress(MultipartFile file,String fileName, String path) throws IOException {
        if (file == null){
            throw CustomException.createCustomException(ExceptionCodeEnum.SYSTEM_FILE_READ_EXCEPTION);
        }
        byte[] bytes = PicUtils.compressPicForScale(file.getBytes(), 60);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        FileItem fileItem = FileUtil.createFileItem(byteArrayInputStream, fileName);
        return saveFile(new CommonsMultipartFile(fileItem),fileName, path);
    }

    /**
     * 删除文件（参数为路径）
     * @param path 文件路径例如（/home/zhj/group/119/8282651/7cs.webp）
     */
    public static void deleteFileOfPath(String path){
            File file = new File(path);
            if (file.delete()){
                log.info("删除文件-" + path + "-成功");
            }
    }

    /**
     * 删除文件（参数为路径列表）
     * @param path 路径列表
     */
    public static void deleteFileOfPath(List<String> path){
       path.forEach(FileUtil::deleteFileOfPath);
    }



    /**
     * 删除文件（参数为url形式）
     * @param path url形式的参数例如(http://ip:8872/group/119/8282651/7cs.webp)
     */
    public static void deleteFileOfUrlType(String path){
        deleteFileOfPath(getAbsolutePath(path));
    }

    /**
     * 批量删除
     * @param path 路径列表
     */
    public static void deleteFileOfUrlType(List<String> path){
        path.forEach(t->deleteFileOfPath(getAbsolutePath(t)));
    }

    public static String getAbsolutePath(String path){
        if (path == null){
            return "";
        }
          return path.replace(DefaultSrc.getRootUrlPath(),DefaultSrc.getRootFilePath());
    }

    public static FileItem createFileItem(InputStream inputStream, String fileName) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        String textFieldName = "file";
        FileItem item = factory.createItem(textFieldName, MediaType.MULTIPART_FORM_DATA_VALUE, true, fileName);
        int bytesRead = 0;
        byte[] buffer = new byte[10 * 1024 * 1024];
        OutputStream os = null;
        //使用输出流输出输入流的字节
        try {
            os = item.getOutputStream();
            while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            inputStream.close();
        } catch (IOException e) {
            log.error("Stream copy exception", e);
            throw new IllegalArgumentException("文件上传失败");
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    log.error("Stream close exception", e);

                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("Stream close exception", e);
                }
            }
        }

        return item;
    }

    public static boolean checkServeIsLocal(){
        return "local".equals(DefaultSrc.getServeLocation());
    }

    public static String saveFileToLocal(MultipartFile file,String fileName,String path){
        File filePath = new File(path);
        System.out.println("成功进入");
        if (!filePath.isDirectory()){
            if (filePath.mkdir()){
                log.info("创建目录成功");
            }
        }
        String fileNameAbsolute = path + File.separator + fileName;
        System.out.println(fileNameAbsolute);
        File fileRet = new File(fileNameAbsolute);
        for (int i = 1; i < 1000; i++) {
            if (fileRet.exists()){
           //     if (fileRet.delete()){
                   fileRet = new File(FileUtil.createFileNameWhenFileRepetitive(fileNameAbsolute,i));
              //  }
            }else {
                try {
                    FileUtils.copyInputStreamToFile(file.getInputStream(),fileRet);
                    break;
                }catch (Exception e){
                    log.error("文件流书写失败");
                }
            }
        }

        return fileRet.getName();
    }

    public static List<FileResource> getMusicResource(){
        String path = DefaultSrc.getRootMusicFile();
        String urlSuffix = DefaultSrc.getMusicSuffix();
        int suffixLength = path.length();
        List<File> fileList = getFileByPath(path);
        List<FileResource> ret = new ArrayList<>();
        for (File file : fileList) {
            FileResource fileResource = new FileResource();
            fileResource.setFileName(file.getName().substring(0,file.getName().indexOf(".")));
            fileResource.setUrl(urlSuffix + file.getAbsolutePath().substring(suffixLength));
            ret.add(fileResource);
        }
        return ret;
    }

    public static List<File> getFileByPath(String path){
        File file = new File(path);
        List<File> list = new ArrayList<>();
        if (file.isFile()){
            list.add(file);
            return list;
        }
        if (file.listFiles() == null){
            return list;
        }

        Deque<File> deque = new ArrayDeque<>(Arrays.asList(Objects.requireNonNull(file.listFiles())));

        while(!deque.isEmpty()){
            File tempFile = deque.poll();
            if (tempFile.isFile()){
                list.add(tempFile);
            }

            if (tempFile.isDirectory()){
                deque.addAll(Arrays.asList(Objects.requireNonNull(tempFile.listFiles())));
            }

        }
        return list;

    }

   public static String addFileByName(MultipartFile file, HttpServletRequest request){
       String fileName = file.getOriginalFilename();
       String path = request.getHeader("path");
       return  saveFileToLocal(file,fileName,path);
   }

   public static boolean deleteFileOfServer(HttpServletRequest request){
       String filePathList = request.getHeader("filePathList");
       try {
           List<String> list = JSON.parseArray(filePathList, String.class);
           deleteFileOfUrlType(list);
       }catch (Exception e){
           e.printStackTrace();
           log.error("文件执行错误");
       }
       return true;
   }

    public static void deleteFile(List<String> path){
        if (checkServeIsLocal()){
            deleteFileOfUrlType(path);
        }else{
            Map<String,String> map = new HashMap<>(4);
            map.put("filePathList",JSON.toJSONString(path));
            map.put("authorization", UserContext.getAuthorization());
            HttpUtil.sendPostWithFile(null,DefaultSrc.getServeAddress() + "deleteFileByName",map);
        }
    }

    public static void deleteDir(File file,int index) {
        if (file.listFiles() == null){
            return;
        }
        // 遍历文件
        for (File item : Objects.requireNonNull(file.listFiles())) {
            // 如果是文件删除文件
            if (item.isFile()) {
                item.delete();
            } else { // 文件夹，回调当前方法
                deleteDir(item,index+1);
            }
        }
        // 删除文件夹
        if (index != 0){
            file.delete();
        }

    }

    public static boolean codeFile(MultipartFile file,String type) throws Exception {
        if (file == null || file.getOriginalFilename() == null || !file.getOriginalFilename().endsWith(".zip")){
            return false;
        }
        String path = "/home/zhj/zip";
        File fileDir = new File(path);
        if (!fileDir.exists()){
            if (!fileDir.mkdirs()){
                System.out.println("文件目录创建失败");
            }
        }
        System.out.println("开始清理目录信息");
        FileUtil.deleteDir(new File("/home/zhj/zip"),0);
//        Runtime.getRuntime().exec(new String[]{"sh","-c","/home/zhj/shell/clean_zip.sh"}).waitFor();
        System.out.println("开始拷贝文件");
        File convFile = new File("/home/zhj/zip" + File.separator + file.getOriginalFilename());
        FileUtils.copyInputStreamToFile(file.getInputStream(), convFile);
        System.out.println("拷贝成功");
        if ("0".equals(type)) {
            Process exec = Runtime.getRuntime().exec(new String[]{"sh","-c","/home/zhj/shell/start_new_server.sh"});
        }else if ("1".equals(type)){
            Process exec = Runtime.getRuntime().exec(new String[]{"sh","-c","/home/zhj/shell/package_and_start_vue.sh"});
        }else if ("2".equals(type)){
            Process exec = Runtime.getRuntime().exec(new String[]{"sh","-c","/home/zhj/shell/package_and_start_java.sh"});
        }
//        for (String s : list) {
//            System.out.println(s);
//        }
//        if (list.size() > 0){
////            LightChatContext.zhj.close();
//            try {
//                startNewServe(list);
//            }catch (Exception e){
//
//            }
//
//            }
          return true;
    }

    public static void submitCodeFile(MultipartFile file,String type) throws Exception {
        log.info("开始进行代码上传,文件的名字为"+file.getOriginalFilename());
        if (!FileUtil.checkServeIsLocal()){
            Map<String,String> map = new HashMap<>();
            map.put("authorization",UserContext.getAuthorization());
            map.put("fileType",type);
            HttpUtil.sendPostWithFile(file.getInputStream(),DefaultSrc.getServeAddress() + "codeServe",map);
        }else{
            FileUtil.codeFile(file,type);
        }

    }

    public static String shCode(String str){
//        return new String[]{"sh", "-c",str};
        return str;
    }

    public static void printProcess(Process process){
        InputStreamReader  isr = new InputStreamReader(process.getInputStream(), Charset.forName("GBK"));
        BufferedReader br = new BufferedReader(isr);
        try {
            String line = "";
            while ((line = br.readLine()) != null) {
                System.out.println("line = " + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                br.close();
                isr.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public static void startNewServe(List<String> list) throws Exception {
        String vueTrust = "move " + zipFile + " to " + vuePath;
        System.out.println("vueTrust-" + vueTrust);
//            Process dist = Runtime.getRuntime().exec(vueTrust);
        List<String> jarList = list.stream().filter(t -> t.endsWith(".jar")).collect(Collectors.toList());
        if (jarList.size() > 0) {
//                LightChatContext.zhj.close();
            String javaTrust = "move " + fileSuffix + File.separator + jarList.get(0) + " to " + javaPath;
            String javaRun = "nohup java -jar " + javaPath + File.separator + jarList.get(0) + " >log.txt";
            System.out.println("javaTrust-" + javaTrust);
            System.out.println("javaRun-" + javaRun);
            Process execFirst = Runtime.getRuntime().exec(shCode(javaTrust));
            printProcess(execFirst);
            int i = execFirst.waitFor();
//            Process execSecond = Runtime.getRuntime().exec(shCode(javaRun));
//            printProcess(execSecond);
//            int j = execSecond.waitFor();
            Process execThird = Runtime.getRuntime().exec(shCode(vueTrust));
            printProcess(execThird);
            int k = execThird.waitFor();
        }
    }

    public static String createFileNameWhenFileRepetitive(String fileName,int i){
        int splitIndex = fileName.lastIndexOf('.');
        if (splitIndex == -1){
            return fileName + "(" + i + ")";
        }

        String prefix = fileName.substring(0,splitIndex);
        String suffix = fileName.substring(splitIndex);

        return prefix + "(" + i + ")" + suffix;
    }

}
