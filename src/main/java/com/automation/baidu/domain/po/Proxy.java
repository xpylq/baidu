/**generator自动生成类，不允许随意修改，添加字段*/
package com.automation.baidu.domain.po;

public class Proxy {
    private Long id;

    private String ip;

    private Integer port;

    //相应速度(秒)
    private Double speed;

    //位置
    private String location;

    public Proxy() {
    }

    public Proxy(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}