package com.review.common.httpclient;

import org.apache.http.HttpResponse;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * httclient构建
 */
public class HttpClientBuilder {

	private static Logger logger = LoggerFactory.getLogger(HttpClientBuilder.class);
    private PoolingHttpClientConnectionManager connectionManager;

    private SSLConnectionSocketFactory sslf = null;
    
    public HttpClientBuilder() {
    	sslf = buildSSLConnectionSocketFactory();
        Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", sslf)
                .build();
        connectionManager = new PoolingHttpClientConnectionManager(reg);
        connectionManager.setDefaultMaxPerRoute(100);
    }

	private SSLConnectionSocketFactory buildSSLConnectionSocketFactory() {
		try {
			/**添加参数 NoopHostnameVerifier.INSTANCE， 解决javax.net.ssl.SSLPeerUnverifiedException: Host name 'zj.zjol.com.cn' does not match the certificate subject provided by the peer**/
			return new SSLConnectionSocketFactory(createIgnoreVerifySSL(), NoopHostnameVerifier.INSTANCE);// 优先绕过安全证书
		} catch (KeyManagementException e) {
			logger.error("ssl connection fail", e);
		} catch (NoSuchAlgorithmException e) {
			logger.error("ssl connection fail", e);
		} catch (KeyStoreException e) {
			logger.error("ssl connection fail", e);
		} 
		return SSLConnectionSocketFactory.getSocketFactory();
	}

	private SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
		// 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
			X509TrustManager trustManager = new X509TrustManager() {

				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				
			};
			
			String protocols = "TLSv1.2";
			
			if(System.getProperty("https.protocols") != null) {
				protocols = System.getProperty("https.protocols");
			}
			SSLContext sc = SSLContext.getInstance(protocols);
			sc.init(null, new TrustManager[] { trustManager }, null);
			return sc;
	}
    
    public HttpClientBuilder setPoolSize(int poolSize) {
        connectionManager.setMaxTotal(poolSize);
        return this;
    }

	public CloseableHttpClient generateClient() {
        CredentialsProvider credsProvider = null;
        org.apache.http.impl.client.HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setConnectionManager(connectionManager);
        //解决post/redirect/post 302跳转问题
        httpClientBuilder.setRedirectStrategy(new CustomRedirectStrategy());
        /**add by wwq 解决javax.net.ssl.SSLPeerUnverifiedException: Host name 'zj.zjol.com.cn' does not match the certificate subject provided by the peer (CN=www.notexist.com)**/
        httpClientBuilder.setSSLSocketFactory(sslf);
        /** end ***/
        SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(60000).setSoKeepAlive(true).setTcpNoDelay(true).build();
        httpClientBuilder.setDefaultSocketConfig(socketConfig);
        connectionManager.setDefaultSocketConfig(socketConfig);
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(5, true));
        return httpClientBuilder.build();
    }

    public static void main(String[] args) throws IOException {
        HttpClientBuilder httpClientBuilder = new HttpClientBuilder();
		HttpClient httpClient = httpClientBuilder.generateClient();
		new HttpGet();
		HttpResponse httpResponse = httpClient.execute(new HttpGet("https://mp.weixin.qq.com/mp/profile_ext?action=report&uin=777&key=777&pass_ticket=BIX/09deKbD1T5Pj1s4glPm3BH6N8d5+H8qdoJt6HE7tyR6A/wYY+OWfRJCPuxoM&username=&useraction=9&t=0.09802238034779864&scene=&__biz=MzIzMDExMjgxOA==&is_ok=1&fromplatform=0"));
    	System.out.println(EntityUtils.toString(httpResponse.getEntity(), "utf-8"));
    }
}
