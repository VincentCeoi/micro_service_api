package com.lazada.microservice.service;

import com.lazada.microservice.core.Service;
import com.lazada.microservice.model.LogApi;

import java.util.List;


/**
 * Created by CodeGenerator on 2018/05/23.
 */
public interface LogApiService extends Service<LogApi> {
    List<LogApi> queryLogList(String nameParam, String startDate, String endDate);

}
