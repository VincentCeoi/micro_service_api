package com.lazada.microservice.service.impl;


import com.lazada.microservice.core.AbstractService;
import com.lazada.microservice.mapper.CityMapper;
import com.lazada.microservice.model.City;
import com.lazada.microservice.service.CityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2017/11/14.
 */
@Service
@Transactional
public class CityServiceImpl extends AbstractService<City> implements CityService {
    @Resource
    private CityMapper cityMapper;




}
