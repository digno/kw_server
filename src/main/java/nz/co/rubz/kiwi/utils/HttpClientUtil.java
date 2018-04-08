package nz.co.rubz.kiwi.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.log4j.Logger;


public class HttpClientUtil {
	private static Logger log = Logger.getLogger(HttpClientUtil.class);

	public static String getHttpContent(String url, String charSet) {

		String result = "";
		HttpParams params = new BasicHttpParams();
//		ConnManagerParams.setMaxTotalConnections(params, 100);
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "UTF-8");
        HttpProtocolParams.setUseExpectContinue(params, true);

		// Create and initialize scheme registry
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory
				.getSocketFactory()));

		PoolingClientConnectionManager tsc = new PoolingClientConnectionManager(schemeRegistry);
		tsc.setMaxTotal(5);
		HttpClient clientUtil = new DefaultHttpClient(tsc, params);
		//设置请求超时
		clientUtil.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000); 
		//设置读取超时
		clientUtil.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000); 
		
		HttpGet httpget = new HttpGet(url);
		try {
			
			HttpResponse response = clientUtil.execute(httpget);
			HttpEntity entity = response.getEntity();
			log.info("----------------------------------------");
			log.debug(response.getStatusLine().toString());
			if (response.getStatusLine().getStatusCode() == 200) {
				if (entity != null) {
					log.debug("Response content length: "
							+ entity.getContentLength());
					InputStream is = entity.getContent();
					ByteArrayOutputStream bo = new ByteArrayOutputStream();
					int i = -1;
					while ((i = is.read()) != -1) {
						bo.write(i);
					}
					result = new String(bo.toByteArray(), charSet);
					log.info(result);
				}
			}
			log.info("----------------------------------------");
		} catch (Exception e) {
			httpget.abort();
			log.error(e.getMessage());
			e.printStackTrace();
		}finally{
           try{
              if(clientUtil !=null){
            	  clientUtil.getConnectionManager().closeExpiredConnections();
            	  //关闭空闲超过10秒的连接
            	  clientUtil.getConnectionManager().closeIdleConnections(10,
      					TimeUnit.SECONDS);
                  clientUtil.getConnectionManager().shutdown();
              }
           }catch(Exception e1){
              e1.printStackTrace();
           }
        }
		return result;
	}
	
	public static String getHttpContentByPost(String url, String paramsString,
			String charSet) {
		HttpResponse response = null;
		HttpEntity entity = null;
		HttpClient httpclient = null;
		String result = "";
		HttpParams params = new BasicHttpParams();
//		ConnManagerParams.setMaxTotalConnections(params, 100);
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "UTF-8");
        HttpProtocolParams.setUseExpectContinue(params, true);

		// Create and initialize scheme registry
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory
				.getSocketFactory()));

		PoolingClientConnectionManager tsc = new PoolingClientConnectionManager(schemeRegistry);
		tsc.setMaxTotal(5);
		try {
			httpclient = new DefaultHttpClient(tsc,params);
			//设置请求超时
			httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000); 
			//设置读取超时
			httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000); 
			//设置代理地址
//			HttpHost proxy = new HttpHost("10.60.8.20", 8080);    
//		    httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);  
			// 目标地址
			HttpPost httppost = new HttpPost(url);
			log.info("request: " + httppost.getRequestLine());
			// 构造最简单的字符串数据
			StringEntity reqEntity = new StringEntity(paramsString,"utf-8");
			// 设置类型
			reqEntity.setContentType("application/x-www-form-urlencoded;charset=UTF-8");
			// 设置请求的数据
			httppost.setEntity(reqEntity);
			// 执行
			response = httpclient.execute(httppost);
			entity = response.getEntity();
			log.info("----------------------------------------");
			log.debug(response.getStatusLine().toString());
			if (response.getStatusLine().getStatusCode() == 200) {
				if (entity != null) {
					log.debug("Response content length: "
							+ entity.getContentLength());
					InputStream is = entity.getContent();
					ByteArrayOutputStream bo = new ByteArrayOutputStream();
					int i = -1;
					while ((i = is.read()) != -1) {
						bo.write(i);
					}
					result = new String(bo.toByteArray(), charSet);
					log.info(result);
				}
			}
			log.info("----------------------------------------");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().closeExpiredConnections();
			//关闭空闲超过10秒的连接
			httpclient.getConnectionManager().closeIdleConnections(10,
					TimeUnit.SECONDS);
			httpclient.getConnectionManager().shutdown();
		}
		return result;
	}
}
