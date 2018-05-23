package com.lazada.microservice.mapper;

import com.lazada.microservice.core.Mapper;
import com.lazada.microservice.model.LogApi;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LogApiMapper extends Mapper<LogApi> {
    List<LogApi> queryLogList(@Param("name") String name, @Param("startDate") String startDate, @Param("endDate") String endDate);
}