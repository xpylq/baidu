package com.automation.baidu.utils;

import com.automation.baidu.domain.po.Proxy;
import com.google.common.base.Strings;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.URI;
import java.text.MessageFormat;

/**
 * Created by youzhihao on 2018/6/1.
 */
public class HttpUtil {


    public static CloseableHttpResponse executeGet(String url) {
        return executeGet(url, null);
    }

    public static CloseableHttpResponse executeGet(String url, Proxy proxy) {
        CloseableHttpResponse response = null;
        try {
            if (Strings.isNullOrEmpty(url)) {
                return null;
            }
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpGet = createHttpGet(proxy);
            httpGet.setURI(new URI(url));
            response = httpclient.execute(httpGet);
        } catch (Exception e) {
            System.out.println(MessageFormat.format("访问[{0}]失败,exception={1}", url, e.toString()));
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
    }

    private static HttpGet createHttpGet(Proxy proxy) {
        HttpGet httpGet = new HttpGet();
        RequestConfig.Builder build =
                RequestConfig.custom()
                        .setConnectTimeout(10000)
                        .setSocketTimeout(10000);
        //换代理重试
        if (proxy != null) {
            build.setProxy(new HttpHost(proxy.getIp(), proxy.getPort()));
        }
        RequestConfig config = build.build();
        httpGet.setConfig(config);
        httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        httpGet.addHeader("Accept-Encoding", "gzip, deflate, br");
        httpGet.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
        httpGet.addHeader("Cache-Control:", "max-age=0");
        httpGet.addHeader("Connection", "keep-alive");
        httpGet.addHeader("Upgrade-Insecure-Requests", "1");
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36");
        return httpGet;
    }
}
