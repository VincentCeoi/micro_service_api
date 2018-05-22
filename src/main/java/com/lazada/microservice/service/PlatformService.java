package com.lazada.microservice.service;

import com.lazada.microservice.core.Service;
import com.lazada.microservice.model.Platform;

import java.util.List;


/**
 * Created by CodeGenerator on 2018/05/21.
 */
public interface PlatformService extends Service<Platform> {
    List<Platform> queryPlatformList(String name);

}
