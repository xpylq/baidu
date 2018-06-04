package com.automation.baidu.utils;

/**
 * Created by youzhihao on 2018/6/4.
 */
public class ThreadUtil {

    public static void sleep(int second){
        try {
            Thread.currentThread().sleep(second * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
