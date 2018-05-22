package com.lazada.microservice.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lazada.microservice.constans.BasicResult;
import com.lazada.microservice.constans.LayUIResult;
import com.lazada.microservice.model.Platform;
import com.lazada.microservice.request.PlatformReq;
import com.lazada.microservice.service.PlatformService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 第三方平台管理
* Created by CodeGenerator on 2018/05/21.
*/
@RestController
@RequestMapping("/platform")
public class PlatformController {

    @Resource
    private PlatformService platformService;

    @Resource
    private BasicResult basicResult;

    @Resource
    private LayUIResult result;

    @PostMapping("/addPlatform")
    public BasicResult add(PlatformReq platformReq) {
        Platform platform = new Platform();
        BeanUtils.copyProperties(platformReq,platform);
        platformService.save(platform);
        basicResult.setMsg("数据添加成功！");
        basicResult.setCode(10000);
        return basicResult;
    }

    @RequestMapping("/delete/{id}")
    public BasicResult delete(@PathVariable Integer id) {
        platformService.deleteById(id);
        basicResult.setMsg("数据删除成功！");
        basicResult.setCode(10000);
        return basicResult;
    }

    @RequestMapping("/deleteByIds")
    public BasicResult deleteByIds(String ids) {
        platformService.deleteByIds(ids);
        basicResult.setMsg("数据删除成功！");
        basicResult.setCode(10000);
        return basicResult;
    }

    @PostMapping("/update")
    public BasicResult update(Platform platform) {
        Integer id = platform.getId();
        Platform platformById = platformService.findById(id);
        if (platformById == null) {
            basicResult.setMsg("数据不存在！");
            basicResult.setCode(10001);
            return basicResult;

        }
        platformById.setName(platform.getName());
        platformById.setTradingName(platform.getTradingName());
        platformById.setApiKey(platform.getApiKey());
        platformById.setLinehaulCode(platform.getLinehaulCode());
        platformById.setStatus(platform.getStatus());
        platformById.setUpdatedAt(new Date());

        platformService.update(platformById);
        basicResult.setMsg("数据更新成功！");
        basicResult.setCode(10000);
        return basicResult;
    }

    @RequestMapping("/platformList")
    public LayUIResult list(@RequestParam(defaultValue = "1") Integer page,
                            @RequestParam(defaultValue = "10") Integer limit,
                            @RequestParam(required = false) String nameParam) {
        Page<Platform> pageInfo = PageHelper.startPage(page, limit);
        List<Platform> list = platformService.queryPlatformList(nameParam);
        //填充数据
        result.setCode(0);
        result.setMsg("查询用户列表！");
        result.setCount((int) pageInfo.getTotal());
        //填充集合数据
        result.setData(list);
        return result;
    }
}
