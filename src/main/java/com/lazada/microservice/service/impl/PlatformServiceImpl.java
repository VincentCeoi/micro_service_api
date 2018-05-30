package com.lazada.microservice.service.impl;

import com.lazada.microservice.mapper.PlatformMapper;
import com.lazada.microservice.model.Platform;
import com.lazada.microservice.service.PlatformService;
import com.lazada.microservice.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by CodeGenerator on 2018/05/21.
 */
@Service
@Transactional
public class PlatformServiceImpl extends AbstractService<Platform> implements PlatformService {
    @Resource
    private PlatformMapper platformMapper;

    @Override
    public List<Platform> queryPlatformList(String name) {
        return platformMapper.queryPlatformList(name);
    }
}
