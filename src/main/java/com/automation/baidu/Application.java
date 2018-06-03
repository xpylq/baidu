package com.automation.baidu;

/**
 * Created by youzhihao on 2017/4/26.
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 项目启动类
 * -Dspring.profiles.active=dev
 * @author youzhihao
 */
@EnableAutoConfiguration
@EnableTransactionManagement
@ComponentScan(basePackages = "com.automation.baidu")
@PropertySources(@PropertySource({"classpath:config/${spring.profiles.active}/applicationContext.properties"}))
@ImportResource({"classpath:config/applicationContext.xml"})
public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("webdriver.chrome.driver","C:\\Users\\PC2018\\AppData\\Local\\Google\\Chrome\\Application\\chromedriver.exe");
        SpringApplication.run(Application.class, args);
    }
}
