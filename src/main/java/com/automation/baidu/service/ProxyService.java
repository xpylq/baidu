package com.automation.baidu.service;

import com.automation.baidu.domain.po.Proxy;
import com.automation.baidu.utils.ProxyUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by youzhihao on 2016/10/17.
 */
@Service
public class ProxyService implements Runnable {

    //免费代理的网址
    public static String url = "https://www.kuaidaili.com/free/intr/{0}/";

    private WebDriver driver;

    public ProxyService() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
    }

    @PostConstruct
    public void init() {
        Executors.newFixedThreadPool(1).submit(this);
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

    @Override
    public void run() {
        parse();
    }
}
