package com.lazada.microservice.service.impl;

import com.lazada.microservice.mapper.DataXMapper;
import com.lazada.microservice.model.DataX;
import com.lazada.microservice.service.DataXService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class DataXServiceImpl implements DataXService {

    @Resource
    private DataXMapper dataXMapper;


    @Override
    public List<DataX> queryList(Map<String, Object> param) {
        return dataXMapper.queryList(param);
    }

    @Override
    public List<DataX> queryDataXByExpress(String expression) {
        return dataXMapper.queryDataXByExpress(expression);
    }

    @Override
    public Integer saveDatax(DataX dataX) {
        return dataXMapper.saveDatax(dataX);
    }

    @Override
    public Integer updateDatax(DataX dataX) {
        return dataXMapper.updateDatax(dataX);
    }

    @Override
    public Integer deleteById(Integer id) {
        return dataXMapper.deleteById(id);
    }

    @Override
    public Integer updateDataXByStatus(Map<String, Object> param) {
        return dataXMapper.updateDataXByStatus(param);
    }
}
