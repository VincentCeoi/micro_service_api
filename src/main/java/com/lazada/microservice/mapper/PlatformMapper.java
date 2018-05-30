package com.lazada.microservice.mapper;

import com.lazada.microservice.core.Mapper;
import com.lazada.microservice.model.Platform;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlatformMapper extends Mapper<Platform> {
    List<Platform> queryPlatformList(@Param("name") String name);

    void updateInvalidById(Integer id);

    void updateInvalidByIds(@Param("ids") List<Integer> ids);
}