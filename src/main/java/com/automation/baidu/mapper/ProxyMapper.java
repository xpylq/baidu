/**generator自动生成类，不允许随意修改，添加字段*/
package com.automation.baidu.mapper;

import com.automation.baidu.domain.po.Proxy;
import com.automation.baidu.domain.po.ProxyExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProxyMapper {
    long countByExample(ProxyExample example);

    int deleteByExample(ProxyExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Proxy record);

    int insertSelective(Proxy record);

    List<Proxy> selectByExample(ProxyExample example);

    Proxy selectOneByExample(ProxyExample example);

    Proxy selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Proxy record, @Param("example") ProxyExample example);

    int updateByExample(@Param("record") Proxy record, @Param("example") ProxyExample example);

    int updateByPrimaryKeySelective(Proxy record);

    int updateByPrimaryKey(Proxy record);
}