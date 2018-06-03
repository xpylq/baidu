package com.automation.baidu.service;

import com.automation.baidu.dao.ProxyDao;
import com.automation.baidu.domain.po.Proxy;
import com.automation.baidu.utils.ChromeDriverUtil;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by youzhihao on 2018/6/2.
 */
@Service
public class HelideckService implements Runnable {

    @Autowired
    private ProxyDao proxyDao;

    private String url = "https://www.baidu.com/";

    private String keyword = "直升机坪";

    private String snapshotPath = "/Users/youzhihao/Downloads/snapshot/{0}.png";


    private Set<String> notClickSet = new HashSet<>();

    @PostConstruct
    public void init() {
        notClickSet.add("www.zsjpsj.com");
        notClickSet.add("www.sfzhsy.com");
        notClickSet.add("www.hkzadc.com");
        notClickSet.add("www.jzjcjs.com");
        notClickSet.add("www.yinghuagd.com");
        notClickSet.add("gdboda.1688.com");
        run();
    }

    @Override
    public void run() {
        List<Proxy> proxyList = proxyDao.selectAll();
        ChromeDriver driver = null;
        for (Proxy proxy : proxyList) {
            try {
                driver = ChromeDriverUtil.create(proxy);
                driver.get(url);
                WebElement input = driver.findElement(By.id("kw"));
                WebElement searchButton = driver.findElement(By.id("su"));
                input.sendKeys(keyword);
                searchButton.submit();
                String windowName = driver.getWindowHandle();
                new WebDriverWait(driver, 20).until(ExpectedConditions.presenceOfElementLocated(By.id("content_left")));
                List<WebElement> resultList = driver.findElements(By.cssSelector("#content_left a[data-is-main-url=true]"));
                driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
                for (WebElement result : resultList) {
                    String mainPage = result.getAttribute("data-landurl");
                    if (notClickSet.contains(mainPage)) {
                        continue;
                    }
                    result.click();
                    driver.switchTo().window(windowName);
                    String fileName = new java.net.URL(mainPage).getHost() + "|" + proxy.getIp();
                    File file = driver.getScreenshotAs(OutputType.FILE);
                    FileUtils.copyFile(file, new File(MessageFormat.format(snapshotPath, fileName)));
                }

            } catch (Exception e) {
                proxyDao.deleteByPrimaryKey(proxy.getId());
            } finally {
                driver.quit();
            }

        }
    }
}
