package com.jumian.wechat.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class WechatHttpUtil {

    public static String httpPost(String strUrl, String postStr) throws Exception {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection connection = (HttpURLConnection)
                    url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            out.append(postStr);
            out.flush();
            out.close();
            int length = (int) connection.getContentLength();
            InputStream is = connection.getInputStream();
            if (length != -1) {
                byte[] data = new byte[length];
                byte[] temp = new byte[512];
                int readLen = 0;
                int destPos = 0;
                while ((readLen = is.read(temp)) > 0) {
                    System.arraycopy(temp, 0, data, destPos, readLen);
                    destPos += readLen;
                }
                String result = new String(data, "UTF-8");
                return result;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "error";
    }

    public static byte[] download(String strUrl, String postStr) {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection connection = (HttpURLConnection)
                    url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            out.append(postStr);
            out.flush();
            out.close();
            int length = (int) connection.getContentLength();
            InputStream is = connection.getInputStream();
            if (length != -1) {
                byte[] data = new byte[length];
                byte[] temp = new byte[512];
                int readLen = 0;
                int destPos = 0;
                while ((readLen = is.read(temp)) > 0) {
                    System.arraycopy(temp, 0, data, destPos, readLen);
                    destPos += readLen;
                }
                return data;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    public static String upload(String urlStr, String fileName, InputStream ins) throws Exception {

        HttpURLConnection connection = null;
        OutputStream out = null;
        DataInputStream dis = null;
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setConnectTimeout(20000);

            // 设置请求信息
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset", "UTF-8");

            // 设置boundary：request头和上传文件内容的分隔符
            String BOUNDARY = "----------" + System.currentTimeMillis();
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            // 请求的正文,获取输出流写入正文内容
            out = new DataOutputStream(connection.getOutputStream());

            // 第一部分：请求的参数处理
            StringBuffer sb = new StringBuffer();
            sb.append("--"); // 必须多两道线
            sb.append(BOUNDARY).append("\r\n");
            sb.append("Content-Disposition: form-data;name=\"media\";filename=\"" + fileName + "\"\r\n");
            sb.append("Content-Type:application/octet-stream\r\n\r\n");
            byte[] head = sb.toString().getBytes();
            out.write(head);

            // 第二部分：文件数据流处理
            int len = 0;
            dis = new DataInputStream(ins);
            byte[] chunk = new byte[8096];
            while ((len = dis.read(chunk)) != -1) {
                out.write(chunk, 0, len);
            }

            // 第三部分：结尾处理，定义最后数据分隔线
            byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");
            out.write(foot);
            out.flush();

            // 获取http请求的响应数据流
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }

            // 将响应结果解析成json字符创返回
            return result;
        } finally {
            if (null != connection)
                connection.disconnect();

            if (null != bufferedReader)
                bufferedReader.close();

            if (null != ins)
                ins.close();

            if (null != out)
                out.close();
        }
    }

}
