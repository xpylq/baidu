package com.automation.baidu.service;

import com.alibaba.fastjson.JSONObject;
import com.automation.baidu.dao.ProxyDao;
import com.automation.baidu.domain.po.Proxy;
import com.automation.baidu.utils.ProxyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by youzhihao on 2018/6/1.
 */
@Service
public class FilterProxyService implements Runnable {

    private static Logger logger = LoggerFactory.getLogger("automation");


    @Autowired
    private ProxyDao proxyDao;

    @PostConstruct
    public void init() {
//        ExecutorService filterExecutor = Executors.newFixedThreadPool(10);
//        for (int i = 0; i < 10; i++) {
//            filterExecutor.submit(this);
//        }
    }

    @Override
    public void run() {
        while (true) {
            Proxy proxy = ProxyUtil.take();
            boolean isValidate = ProxyUtil.isValidateProxy(proxy);
            if (isValidate) {
                Proxy temp = proxyDao.selectByIpAndPort(proxy.getIp(), proxy.getPort());
                if (temp == null) {
                    proxyDao.insertSelective(proxy);
                    logger.info("[op:过滤代理] {}", JSONObject.toJSON(proxy));
                }
            }
        }
    }
}
