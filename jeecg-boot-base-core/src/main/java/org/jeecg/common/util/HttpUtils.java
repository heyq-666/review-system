package org.jeecg.common.util;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * @author javabage
 * @date 2023/3/24
 */
public class HttpUtils {

    public static Logger logger = LoggerFactory.getLogger(WxAppletsUtils.class);

    public static HttpURLConnection getConnection(String url, String params) throws IOException {
        //初始化连接
        HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(30000);
        //参数输出
        PrintWriter printWriter = new PrintWriter(connection.getOutputStream());
        printWriter.write(params);
        printWriter.flush();
        printWriter.close();
        return connection;
    }

    public static String postString(String url, String params) {
        BufferedInputStream inputStream = null;
        HttpURLConnection connection = null;
        try {
            //初始化连接
            connection = getConnection(url, params);
            //读返回流
            inputStream = new BufferedInputStream(connection.getInputStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int length;
            while ((length = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, length);
            }
            return out.toString();
        } catch (Exception e) {
            logger.error("post error, ", e);
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            IOUtils.closeQuietly(inputStream);
        }
    }

    public static String postFile(String url, String params, String filePath) {
        InputStream inputStream = null;
        HttpURLConnection connection = null;
        try {
            //初始化连接
            connection = getConnection(url, params);
            //读返回流
            inputStream = connection.getInputStream();
            return OssUtils.uploadFile(String.format(filePath, DateUtils.formatDate(new Date())), inputStream);
        } catch (Exception e) {
            logger.error("post error, ", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            IOUtils.closeQuietly(inputStream);
        }
        return null;
    }
}
