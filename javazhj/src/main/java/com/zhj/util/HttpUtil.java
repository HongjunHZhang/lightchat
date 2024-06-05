package com.zhj.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * @author 789
 */
public class HttpUtil {

    public static String sendPostWithFile(InputStream byteArrayInputStream, String urlRet, Map<String, String> map) {
        DataOutputStream out;
        final String newLine = "\r\n";
        final String prefix = "--";
        try {
            URL url = new URL(urlRet);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            String boundary = "-------7da2e536604c8";
            conn.setRequestMethod("POST");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Charsert", "UTF-8");
            map.forEach(conn::setRequestProperty);
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            out = new DataOutputStream(conn.getOutputStream());

            // 添加参数file
            StringBuilder sb1 = new StringBuilder();
            sb1.append(prefix);
            sb1.append(boundary);
            sb1.append(newLine);
            if (byteArrayInputStream != null){
                sb1.append("Content-Disposition: form-data;name=\"file\";filename=\"").append(map.get("fileName")).append("\"").append(newLine);
                sb1.append("Content-Type:application/octet-stream");
                sb1.append(newLine);
                sb1.append(newLine);
            }

            out.write(sb1.toString().getBytes());
            if (byteArrayInputStream != null){
                DataInputStream in = new DataInputStream(byteArrayInputStream);
                byte[] bufferOut = new byte[1024];
                int bytes;
                while ((bytes = in.read(bufferOut)) != -1) {
                    out.write(bufferOut, 0, bytes);
                }
                out.write(newLine.getBytes());
                in.close();
            }

            // 添加参数sysName
            String sb = prefix +
                    boundary +
                    newLine +
                    "Content-Disposition: form-data;name=\"sysName\"" +
                    newLine +
                    newLine +
                    "test";
            out.write(sb.getBytes());

            // 添加参数returnImage
            String sb2 = newLine +
                    prefix +
                    boundary +
                    newLine +
                    "Content-Disposition: form-data;name=\"returnImage\"" +
                    newLine +
                    newLine +
                    "false";
            out.write(sb2.getBytes());

            byte[] endData = ("\r\n--" + boundary + "--\r\n").getBytes();
            // 写上结尾标识
            out.write(endData);
            out.flush();
            out.close();

            // 定义BufferedReader输入流来读取URL的响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder retSb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                retSb.append(line);
            }
             return retSb.toString();
        } catch (Exception e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
            return null;
        }
    }



}
