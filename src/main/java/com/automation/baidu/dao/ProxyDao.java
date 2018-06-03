package com.automation.baidu.dao;

import com.automation.baidu.domain.po.Proxy;
import com.automation.baidu.domain.po.ProxyExample;
import com.automation.baidu.mapper.ProxyMapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProxyDao {

    private static final String nameSpace = ProxyDao.class.getName();

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private ProxyMapper proxyMapper;

    public int insert(Proxy proxy) {
        return proxyMapper.insert(proxy);
    }

    public int insertSelective(Proxy proxy) {
        return proxyMapper.insertSelective(proxy);
    }

    public int deleteByPrimaryKey(Long id) {
        return proxyMapper.deleteByPrimaryKey(id);
    }

    public int updateByPrimaryKey(Proxy proxy) {
        return proxyMapper.updateByPrimaryKey(proxy);
    }

    public int updateByPrimaryKeySelective(Proxy proxy) {
        return proxyMapper.updateByPrimaryKeySelective(proxy);
    }

    public Proxy selectByPrimaryKey(Long id) {
        return proxyMapper.selectByPrimaryKey(id);
    }

    public Proxy selectByIpAndPort(String ip, Integer port) {
        ProxyExample example = new ProxyExample();
        example.createCriteria().andIpEqualTo(ip).andPortEqualTo(port);
        return proxyMapper.selectOneByExample(example);
    }

    public List<Proxy> selectAll() {
        ProxyExample example = new ProxyExample();
        return proxyMapper.selectByExample(example);
    }

}