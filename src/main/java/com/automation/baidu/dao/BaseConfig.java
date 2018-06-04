package com.automation.baidu.dao;

import com.automation.baidu.utils.HttpClientTemplate;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by youzhihao on 2017/4/26.
 */
@Configuration

public class BaseConfig {


    @Bean
    public HttpClientTemplate httpClientTemplate() {
        HttpClientTemplate httpClientTemplate = new HttpClientTemplate();
        httpClientTemplate.setTimeout(50000);
        httpClientTemplate.setDefaultCharset("utf-8");
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(200);
        connectionManager.setDefaultMaxPerRoute(20);
        httpClientTemplate.setConnectionManager(connectionManager);
        httpClientTemplate.init();
        return httpClientTemplate;
    }
}
