package com.lazada.microservice.service.impl;

import com.lazada.microservice.mapper.LogApiMapper;
import com.lazada.microservice.model.LogApi;
import com.lazada.microservice.service.LogApiService;
import com.lazada.microservice.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by CodeGenerator on 2018/05/23.
 */
@Service
@Transactional
public class LogApiServiceImpl extends AbstractService<LogApi> implements LogApiService {
    @Resource
    private LogApiMapper logApiMapper;

    @Override
    public List<LogApi> queryLogList(String nameParam, String startDate, String endDate) {
        return logApiMapper.queryLogList(nameParam,startDate,endDate);
    }
}
