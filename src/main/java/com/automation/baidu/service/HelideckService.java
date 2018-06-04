package com.automation.baidu.service;

import com.automation.baidu.dao.ProxyDao;
import com.automation.baidu.domain.po.Proxy;
import com.automation.baidu.utils.ChromeDriverUtil;
import com.automation.baidu.utils.ThreadUtil;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by youzhihao on 2018/6/2.
 */
@Service
public class HelideckService implements Runnable {

    private static Logger logger = LoggerFactory.getLogger("automation");


    @Autowired
    private ProxyDao proxyDao;

    private String url = "https://www.baidu.com/";

    private String keyword = "直升机坪";

    private String snapshotRootPath = "/Users/youzhihao/Downloads/snapshot/";

    private String snapshotPath = "/Users/youzhihao/Downloads/snapshot/{0}.png";


    private Set<String> notClickSet = new HashSet<>();

    @Autowired
    private ProxyService proxyService;

    @PostConstruct
    public void init() {
        notClickSet.add("www.zsjpsj.com");
        notClickSet.add("www.sfzhsy.com");
        notClickSet.add("www.hkzadc.com");
        notClickSet.add("www.jzjcjs.com");
        notClickSet.add("www.yinghuagd.com");
        notClickSet.add("gdboda.1688.com");
        notClickSet.add("www.gdboda.com.cn");
        Executors.newFixedThreadPool(1).submit(this);
        ThreadUtil.sleep(5);
        Executors.newFixedThreadPool(1).submit(this);
        ThreadUtil.sleep(5);
        Executors.newFixedThreadPool(1).submit(this);
        ThreadUtil.sleep(5);
        Executors.newFixedThreadPool(1).submit(this);
        ThreadUtil.sleep(5);
        Executors.newFixedThreadPool(1).submit(this);
    }

    @Override
    public void run() {
        while (true) {
            if (isDown()) {
                break;
            }
            List<Proxy> proxyList = proxyService.getProxyList();
            ChromeDriver driver = null;
            for (Proxy proxy : proxyList) {
                File proxyImage = null;
                boolean needSaveProxyImage = false;
                try {
                    needSaveProxyImage = false;
                    logger.info("开始点击:代理{}", proxy.getIp() + ":" + proxy.getPort());
                    driver = ChromeDriverUtil.create(proxy);
                    driver.get("http://www.ip138.com/");
                    proxyImage = driver.getScreenshotAs(OutputType.FILE);
                    driver.get(url);
                    WebElement input = driver.findElement(By.id("kw"));
                    WebElement searchButton = driver.findElement(By.id("su"));
                    input.sendKeys(keyword);
                    searchButton.submit();
                    String windowName = driver.getWindowHandle();
                    new WebDriverWait(driver, 20).until(ExpectedConditions.presenceOfElementLocated(By.id("content_left")));
                    List<WebElement> resultList = driver.findElements(By.cssSelector("#content_left a[data-is-main-url=true]"));
                    driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
                    for (WebElement result : resultList) {
                        String mainPage = result.getAttribute("data-landurl");
                        mainPage = new java.net.URL(mainPage).getHost();
                        if (notClickSet.contains(mainPage)) {
                            continue;
                        }
                        try {
                            result.click();
                            ThreadUtil.sleep(30);
                            driver.switchTo().window(windowName);
                            String fileName = proxy.getIp() + "|" + mainPage;
                            snapshot(driver, fileName);
                            needSaveProxyImage = true;
                        } catch (Exception e) {
                            logger.error("resultList循环失败", e.getMessage());
                            continue;
                        }
                    }

                } catch (Exception e) {
                    logger.error("proxy循环失败", e.getMessage());
                    proxyDao.deleteByPrimaryKey(proxy.getId());
                } finally {
                    if (needSaveProxyImage) {
                        try {
                            FileUtils.copyFile(proxyImage, new File(MessageFormat.format(snapshotPath, proxy.getIp())));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    driver.quit();
                }
            }
        }
    }

    public void snapshot(ChromeDriver driver, String fileName) {
        try {
            File file = driver.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(file, new File(MessageFormat.format(snapshotPath, fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //判断是否完成
    public boolean isDown() {
        File file = new File(snapshotRootPath);
        return file.list().length > 250;

    }
}
