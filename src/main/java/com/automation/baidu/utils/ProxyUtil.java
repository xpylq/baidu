package com.automation.baidu.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.automation.baidu.domain.po.Proxy;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    public static List<Proxy> getProxyList() {
        CloseableHttpResponse response = HttpUtil.executeGet("http://api.xdaili.cn/xdaili-api//greatRecharge/getGreatIp?spiderId=4837417faf1f475b8ae49e58aa8ee5fb&orderno=MF2018632339UwIyxb&returnType=2&count=10")
        try {
            HttpEntity entity = response.getEntity();
            String reuslt = EntityUtils.toString(entity);
            JSONArray jsonArray = JSONArray.parseArray(reuslt);
            List<Proxy> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                list.add()
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
