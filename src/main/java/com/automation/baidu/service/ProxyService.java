package com.automation.baidu.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.automation.baidu.domain.po.Proxy;
import com.automation.baidu.utils.HttpClientTemplate;
import com.automation.baidu.utils.HttpUtil;
import com.automation.baidu.utils.ProxyUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by youzhihao on 2016/10/17.
 */
@Service
public class ProxyService implements Runnable {

    private static Logger logger = LoggerFactory.getLogger("automation");

    //免费代理的网址
    public static String url = "https://www.kuaidaili.com/free/intr/{0}/";


    private WebDriver driver;

    @Autowired
    private HttpClientTemplate httpClientTemplate;


    @PostConstruct
    public void init() {
        //driver = new ChromeDriver();
        //driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        //Executors.newFixedThreadPool(1).submit(this);
    }

    public void parse() {
        while (true) {
            for (int i = 1; i <= 1000; i++) {
                try {
                    driver.get(MessageFormat.format(url, i));
                    WebElement tbody = driver.findElement(By.tagName("tbody"));
                    List<WebElement> trs = tbody.findElements(By.tagName("tr"));
                    for (WebElement tr : trs) {
                        String ip = tr.findElement(By.cssSelector("[data-title='IP']")).getText();
                        String port = tr.findElement(By.cssSelector("[data-title='PORT']")).getText();
                        String location = tr.findElement(By.cssSelector("[data-title='位置']")).getText();
                        Proxy proxy = new Proxy();
                        proxy.setIp(ip);
                        proxy.setPort(Integer.valueOf(port));
                        proxy.setLocation(location);
                        ProxyUtil.put(proxy);
                        System.out.println("获取ip:" + ip + ":" + port);
                    }
                    Thread.currentThread().sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
        }
    }

    public List<Proxy> getProxyList() {
        List<Proxy> list = new ArrayList<>();
        try {
            String response = httpClientTemplate.executeGet("http://api.xdaili.cn/xdaili-api//greatRecharge/getGreatIp?spiderId=4837417faf1f475b8ae49e58aa8ee5fb&orderno=YZ2018649601CyHsGu&returnType=2&count=5");
            JSONObject jsonObject = JSON.parseObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("RESULT");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject object = (JSONObject) jsonArray.get(i);
                list.add(new Proxy(object.getString("ip"), object.getInteger("port")));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return list;
        }
        logger.info("[op:getProxyList],proxyList={}", JSONObject.toJSON(list));
        return list;
    }

    @Override
    public void run() {
        parse();
    }
}
