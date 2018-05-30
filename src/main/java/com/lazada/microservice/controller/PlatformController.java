package com.lazada.microservice.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lazada.microservice.constans.BasicResult;
import com.lazada.microservice.constans.LayUIResult;
import com.lazada.microservice.model.Platform;
import com.lazada.microservice.request.PlatformReq;
import com.lazada.microservice.service.PlatformService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * 第三方平台管理
* Created by CodeGenerator on 2018/05/21.
*/
@RestController
@CrossOrigin
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
        Platform platform = platformService.findById(id);
        if (platform == null) {
            return basicResult.fail("数据不存在");
        }
        platformService.updateInvalidById(id);
        return basicResult.success("数据删除成功!");
    }

    @RequestMapping("/deleteByIds")
    public BasicResult deleteByIds(String ids) {
        List<Platform> platformList = platformService.findByIds(ids);
        if (CollectionUtils.isEmpty(platformList)) {
            return basicResult.fail("数据不存在");
        }
        List<Integer> idList = platformList.stream()
                .map(Platform::getId)
                .collect(toList());
        platformService.updateInvalidByIds(idList);

        return basicResult.success("数据删除成功!");
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

    /**
     * 更改平台状态为:启用/禁用
     * @param id
     * @param status
     * @return
     */
    @PostMapping("/updateStatus")
    public BasicResult updateStatus(@RequestParam Integer id, @RequestParam Integer status) {
        Platform platform = platformService.findById(id);
        if (platform == null) {
            return basicResult.fail("数据不存在!");
        }
        platform.setStatus(status);
        platformService.update(platform);
        return basicResult.success("数据更新成功!");
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
