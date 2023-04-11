package com.review.common.httpclient;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientUtils {

    public static String escape(String str) throws Exception {
        if (str == null)
            return null;

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        StringBuffer tmp = new StringBuffer();
        boolean isAscii = true;

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c < 0x80) {
                if (!isAscii) {
                    write(tmp.toString(), os, false);
                    tmp = new StringBuffer();
                }
                isAscii = true;
                tmp.append(c);
            } else {
                if (isAscii) {
                    write(tmp.toString(), os, true);
                    tmp = new StringBuffer();
                }
                isAscii = false;
                tmp.append(c);
            }
        }

        write(tmp.toString(), os, isAscii);

        return os.toString().replaceAll("\\\\", "%");

    }

    public static void write(String str, OutputStream os, boolean isAscii)
            throws IOException {
        // System.out.println("out:" + str);
        if (isAscii) {
            os.write(URLEncoder.encode(str, "UTF-8").getBytes());
        } else {
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                write(c, os);
            }
        }
    }

    public static void write(char ch, OutputStream os) throws IOException {
        if (ch < 0x80)
            os.write(ch);
        else {
            os.write('\\');
            os.write('u');

            int b = (ch >> 12) & 0xf;
            os.write(b < 10 ? b + '0' : b + 'a' - 10);
            b = (ch >> 8) & 0xf;
            os.write(b < 10 ? b + '0' : b + 'a' - 10);
            b = (ch >> 4) & 0xf;
            os.write(b < 10 ? b + '0' : b + 'a' - 10);
            b = ch & 0xf;
            os.write(b < 10 ? b + '0' : b + 'a' - 10);
        }
    }

    /**
     * Default suffix of ommited character.
     */
    private static final String DEFAULT_SUFIX = "……";

    public static final char urlSeparatorChar = '/';
    public static final String urlSeparator = "" + urlSeparatorChar;

    private static final CloseableHttpClient httpClient;
    public static final String CHARSET = "UTF-8";

    static {
       // RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000).build();
        // httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        httpClient = new HttpClientBuilder().generateClient();
    }

    public static String doGet(String url, Map<String, String> params){
        return doGet(url, params,CHARSET);
    }
    public static String doPost(String url, Map<String, String> params){
        return doPost(url, params,CHARSET);
    }
    /**
     * HTTP Get 获取内容
     * @param url  请求的url地址 ?之前的地址
     * @param params 请求的参数
     * @param charset    编码格式
     * @return    页面内容
     */
    public static String doGet(String url,Map<String,String> params,String charset){
        if(StringUtils.isBlank(url)){
            return null;
        }
        try {
            if(params != null && !params.isEmpty()){
                List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
                for(Map.Entry<String,String> entry : params.entrySet()){
                    String value = entry.getValue();
                    if(value != null){
                        pairs.add(new BasicNameValuePair(entry.getKey(),value));
                    }
                }
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
            }
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpGet.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null){
                result = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);
            response.close();
            return result;
        } catch (Exception e) {
            logger.error("doGet error, ", e);
        }
        return null;
    }

    /**
     * HTTP Post 获取内容
     * @param url  请求的url地址 ?之前的地址
     * @param params 请求的参数
     * @param charset    编码格式
     * @return    页面内容
     */
    public static String doPost(String url,Map<String,String> params,String charset){
        if(StringUtils.isBlank(url)){
            return null;
        }
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        try {
            List<NameValuePair> pairs = null;
            if(params != null && !params.isEmpty()){
                pairs = new ArrayList<NameValuePair>(params.size());
                for(Map.Entry<String,String> entry : params.entrySet()){
                    String value = entry.getValue();
                    if(value != null){
                        pairs.add(new BasicNameValuePair(entry.getKey(),value));
                    }
                }
            }
            HttpPost httpPost = new HttpPost(url);
            if(pairs != null && pairs.size() > 0){
                httpPost.setEntity(new UrlEncodedFormEntity(pairs,CHARSET));
            }
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpPost.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
           entity = response.getEntity();
            String result = null;
            if (entity != null){
                result = EntityUtils.toString(entity, charset);
            }
            return result;
        } catch (Exception e) {
            logger.error("doPost error, ", e);
        } finally {
            org.apache.http.client.utils.HttpClientUtils.closeQuietly(response);
            try {
                EntityUtils.consume(entity);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    /**
     * HTTP Post 获取内容
     * @param url  请求的url地址 ?之前的地址
     * @param json 请求的参数
     * @param charset    编码格式
     * @return    页面内容
     */
    public static String doPost(String url, JSONObject json, String charset){
        if(StringUtils.isBlank(url)){
            return null;
        }
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            if(json != null && json.size() > 0){
                StringEntity entity = new StringEntity(json.toString(), charset);
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
            }
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpPost.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null){
                result = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);

            return result;
        } catch (Exception e) {
            logger.error("doPost error, ", e);
        } finally {
            if(response != null) {
                org.apache.http.client.utils.HttpClientUtils.closeQuietly(response);
            }
        }
        return null;
    }

    public static void main(String []args){
        /*String getData = doGet("http://www.oschina.net/",null);
        System.out.println(getData);
        System.out.println("----------------------分割线-----------------------");
        String postData = doPost("http://www.oschina.net/",null);
        System.out.println(postData);*/
        JSONObject params = new JSONObject();
        params.put("uid", "00320092");
        params.put("encodedPasswd", "cctv85068056");
        params.put("salt", "");
        String res = doPost("http://10.116.82.33/ldap-common-service/api/v1/user/encodedVerify", params, "UTF-8");
        System.out.println(res);
    }
}
