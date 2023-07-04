package org.jeecg.common.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * @author javabage
 * @date 2023/3/24
 */
public class OssUtils {

    public static Logger logger = LoggerFactory.getLogger(OssUtils.class);

    public static final ResourceBundle bundle = ResourceBundle.getBundle("oosconfig");

    private static String bucketName = bundle.getString("IMG_BUCHETNAME");

    private static volatile OSS ossClient;

    public static OSS getOssClient() {
        if (ossClient == null) {
            synchronized (OssUtils.class) {
                if (ossClient == null) {
                    ossClient = new OSSClientBuilder().build(bundle.getString("ENDPOINT"), bundle.getString("ACCESS_ID"),
                            bundle.getString("ACCESS_KEY"));
                }
            }
        }
        return ossClient;
    }

    /**
     * 上传文件
     * @param fileName
     * @return
     */
    public static String uploadFile(String fileName, InputStream is) {
        try {
            String filePath = String.format(fileName, DateUtils.formatDate(new Date()));
            getOssClient().putObject(bucketName, filePath, is);
            return filePath;
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
}
