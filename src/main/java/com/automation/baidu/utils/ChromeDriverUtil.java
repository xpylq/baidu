package com.automation.baidu.utils;

import com.automation.baidu.domain.po.Proxy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.concurrent.TimeUnit;

/**
 * Created by youzhihao on 2018/6/2.
 */
public class ChromeDriverUtil {

    public static ChromeDriver create(Proxy proxyModel) {
        //设置代理
        String proxyIpAndPort = proxyModel.getIp() + ":" + proxyModel.getPort();
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability(CapabilityType.ForSeleniumServer.AVOIDING_PROXY, true);
        cap.setCapability(CapabilityType.ForSeleniumServer.ONLY_PROXYING_SELENIUM_TRAFFIC, true);
        org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
        proxy.setHttpProxy(proxyIpAndPort).setFtpProxy(proxyIpAndPort).setSslProxy(proxyIpAndPort);
        cap.setCapability(CapabilityType.PROXY, proxy);
        ChromeDriver driver = new ChromeDriver(cap);
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(15,TimeUnit.SECONDS);
        return driver;
    }

}
