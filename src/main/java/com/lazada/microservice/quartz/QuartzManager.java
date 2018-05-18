package com.lazada.microservice.quartz;

import com.lazada.microservice.model.DataX;
import com.lazada.microservice.util.DataXUtil;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

public class QuartzManager {



    /**
     * 任务调度器
     */
    private static ThreadPoolTaskScheduler threadPoolTaskScheduler= new ThreadPoolTaskScheduler();
    //初始化线程池
    static{
        threadPoolTaskScheduler.initialize();
    }

    /**
     * 任务对象集合
     */
    public static Map<String,ScheduledFuture<?>>  futureMap = new HashMap<>();

    /**
     * 动态添加定时任务
     * @param expression ：表达式
     * @param list ：DataX对象集合
     */
    public static void addJob(String expression,List<DataX> list)throws Exception{
        //创建任务
        ScheduledFuture<?> future;
        //添加任务
        future = threadPoolTaskScheduler.schedule(() -> {
                    //遍历文件名数组
                    for(DataX dataX : list){
                        //调用函数，进行执行同步文件
                        DataXUtil.executeSyc(dataX.getFilename());
                    }
                },
                new CronTrigger(expression));
        //将任务添加到Map 集合中
        futureMap.put(expression,future);
    }

}
