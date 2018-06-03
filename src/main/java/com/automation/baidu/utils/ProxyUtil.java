package com.automation.baidu.utils;

import com.automation.baidu.domain.po.Proxy;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by youzhihao on 2018/6/1.
 */
public class ProxyUtil {

    public static String CHECK_URL = "http://www.baidu.com";

    private static volatile BlockingDeque<Proxy> proxyModelQueue = new LinkedBlockingDeque<>();

    //判断是否是合格的代理
    public static boolean isValidateProxy(Proxy proxy) {
        CloseableHttpResponse response = HttpUtil.executeGet(CHECK_URL, proxy);
        if (response == null || response.getStatusLine().getStatusCode() != 200) {
            return false;
        }
        return true;
    }

    public static Proxy take() {
        try {
            return proxyModelQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void put(Proxy proxyModel) {
        try {
            proxyModelQueue.put(proxyModel);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
