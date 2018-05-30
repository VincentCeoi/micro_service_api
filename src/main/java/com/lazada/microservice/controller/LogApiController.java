package com.lazada.microservice.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lazada.microservice.constans.LayUIResult;
import com.lazada.microservice.model.LogApi;
import com.lazada.microservice.service.LogApiService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by CodeGenerator on 2018/05/23.
 */
@RestController
@RequestMapping("/log")
public class LogApiController {
    @Resource
    private LogApiService logApiService;
    @Resource
    private LayUIResult layUIResult;

    @RequestMapping("/logList")
    public LayUIResult list(@RequestParam(defaultValue = "1") Integer page,
                            @RequestParam(defaultValue = "10") Integer limit,
                            @RequestParam(required = false) String nameParam,
                            @RequestParam(required = false) String startDate,
                            @RequestParam(required = false) String endDate) {
        Page<LogApi> pageInfo = PageHelper.startPage(page, limit);
        List<LogApi> list = logApiService.queryLogList(nameParam,startDate, endDate);
        layUIResult.setCode(0);
        layUIResult.setData(list);
        layUIResult.setCount((int) pageInfo.getTotal());
        return layUIResult;
    }
}
