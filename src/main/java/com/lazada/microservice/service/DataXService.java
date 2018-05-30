package com.lazada.microservice.service;

import com.lazada.microservice.model.DataX;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DataXService {

    /**
     * 根据ID逆序查询
     * @return
     */
    public List<DataX> queryList(Map<String,Object> param);



    public List<DataX> queryDataXByExpress(@Param("expression") String expression);



    public Integer saveDatax(DataX dataX) throws Exception;


    public Integer updateDatax(DataX dataX);

    public Integer deleteById(@Param("id") Integer id);

    /**
     * 批量处理 启用/弃用  文件
     * @param param ：参数集合
     * @return
     */
    public Integer updateDataXByStatus(Map<String,Object> param);

}
