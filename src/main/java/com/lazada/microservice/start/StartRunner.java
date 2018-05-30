package com.lazada.microservice.start;

import com.lazada.microservice.mapper.DataXMapper;
import com.lazada.microservice.model.DataX;
import com.lazada.microservice.quartz.QuartzManager;
import com.lazada.microservice.service.DataXService;
import com.lazada.microservice.util.DataXUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component    //注解标识
@Order(value = 0)     //指向顺序
public class StartRunner implements CommandLineRunner {


    @Resource
    private DataXService dataXService;

    @Value("${datax.exe.path}")
    private String dataXExePath;
    @Value("${datax.json.path}")
    private String dataXJsonPath;

    /**
     * 在程序启动 ，加载时进行调用
     * @param args
     * @throws Exception ：抛出异常
     */
    @Override
    public void run(String... args) throws Exception {
        //将注入的数据，存入静态变量中
        DataXUtil.dataxPath = dataXExePath;
        DataXUtil.jsonPath = dataXJsonPath;
        //调用接口，获取数据 [取状态为 1 ，可用的数据]
        List<DataX> dataXList = dataXService.queryDataXByExpress(null);
        //调用过滤函数
        Map<String,List<DataX>> map = filterExpression(dataXList);
        //遍历map
        for(String expression : map.keySet()){
            //调用创建定时任务方法
            QuartzManager.addJob(expression,map.get(expression));
        }
    }


    /**
     * 过滤数据，进行组装
     * @param dataXList ：数据集合
     * @return
     */
    public Map<String,List<DataX>> filterExpression(List<DataX> dataXList){
        //创建集合
        Map<String,List<DataX>> map = new HashMap<>();
        //判断是否有数据
        if(dataXList != null && dataXList.size() > 0){
            //进行遍历集合
            for(DataX x : dataXList){
                //根据表达式在map中获取，是否存在
                List<DataX> array = map.get(x.getExpression());
                //判断是否为空
                if(array != null && array.size() > 0){
                    //添加元素
                    array.add(x);
                    //存入集合
                    map.put(x.getExpression(),array);
                }else{
                    //存入文件名
                    array = new ArrayList<>();
                    //添加元素
                    array.add(x);
                    //存入集合
                    map.put(x.getExpression(),array);
                }
            }
        }
        return map;
    }

}
