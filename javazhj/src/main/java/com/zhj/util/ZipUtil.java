package com.zhj.util;

import com.zhj.LightChatContext;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author 789
 */
public class ZipUtil {


    public static List<String> unZip(MultipartFile file, String destDirPath) throws RuntimeException, IOException {
        String path = destDirPath + "/" + "temp";
        File fileDir = new File(path);
        if (!fileDir.exists()){
            if (!fileDir.mkdirs()){
                System.out.println("文件目录创建失败");
            }
        }
        System.out.println(path + File.separator + file.getOriginalFilename());
        try {
            System.out.println(2);
            File convFile = new File(file.getOriginalFilename());
            FileUtils.copyInputStreamToFile(file.getInputStream(), convFile);
           return unZip(convFile,destDirPath);
        }catch (Exception e){
            e.printStackTrace();
        }
         return new ArrayList<>();
    }

    /**
     * zip解压
     *
     * @param srcFile     zip源文件
     * @param destDirPath 解压后的目标文件夹
     * @throws RuntimeException 解压失败会抛出运行时异常
     */
    public static List<String> unZip(File srcFile, String destDirPath) throws RuntimeException {
        //记录解压出来的所有文件名
        List<String> filesName = new ArrayList<>();
        long start = System.currentTimeMillis();
        // 判断源文件是否存在
        if (!srcFile.exists()) {
            throw new RuntimeException(srcFile.getPath() + "所指文件不存在");
        }
        // 开始解压
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(srcFile, Charset.forName("GBK"));
            Enumeration<?> entries = zipFile.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                //添加进filesName
                if (entry.getName().endsWith(".jar")){
                    filesName.add(entry.getName());
                }
                System.out.println("解压文件:" + entry.getName());
                // 如果是文件夹，就创建个文件夹
                if (entry.isDirectory() && !entry.getName().endsWith(".jar")) {
                    String dirPath = destDirPath + "/" + entry.getName();
                    File dir = new File(dirPath);
                    if (dir.mkdirs()){
                        System.out.println("解压成功");
                    }
                } else {
                    // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
                    File targetFile = new File(destDirPath + "/" + entry.getName());
                    // 保证这个文件的父文件夹必须要存在
                    if (!targetFile.getParentFile().exists()) {
                        if (targetFile.getParentFile().mkdirs()){
                            System.out.println("创建目录成功");
                        }
                    }
                    if (targetFile.createNewFile()){
                        // 将压缩文件内容写入到这个文件中
                        InputStream is = zipFile.getInputStream(entry);
                        FileOutputStream fos = new FileOutputStream(targetFile);
                        int len;
                        byte[] buf = new byte[1024];
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                        }
                        // 关流顺序，先打开的后关闭
                        fos.close();
                        is.close();
                    }
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("解压完成，耗时：" + (end - start) + " ms");
        } catch (Exception e) {
            throw new RuntimeException("unzip error from ZipUtils", e);
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return filesName;
    }


    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @return 是否删除成功
     */
    public static boolean deleteFile(String filePath) {
        boolean flag = false;
        File file = new File(filePath);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        if (tempList == null){
            return true;
        }
        File temp;
        for (String s : tempList) {
            if (filePath.endsWith(File.separator)) {
                temp = new File(filePath + s);
            } else {
                temp = new File(filePath + File.separator + s);
            }
            if (temp.isFile()) {
                if (temp.delete()) {
                    System.out.println("删除成功");
                }
            }
            if (temp.isDirectory()) {
                // 先删除文件夹里面的文件
                deleteFile(filePath + "/" + s);
                // 再删除空文件夹
                deleteFile(filePath + "/" + s);
                flag = true;
            }
        }
        return flag;
    }


}
