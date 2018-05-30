package com.lazada.microservice.controller;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lazada.microservice.constans.BasicResult;
import com.lazada.microservice.constans.LayUIResult;
import com.lazada.microservice.model.DataX;
import com.lazada.microservice.quartz.QuartzManager;
import com.lazada.microservice.service.DataXService;
import com.lazada.microservice.util.DataXUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ScheduledFuture;

@RestController
@RequestMapping("/dataX")
public class DataXController {

   @Resource
    private DataXService dataXService;
    /**
     * 常规返回对象
     */
   @Resource
   private BasicResult basicResult;

   @Resource
   private LayUIResult layUIResult;

    /**
     * 日期格式对象
     */
   private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取列表
     * @param request
     * @return
     */
    @RequestMapping("/queryDataXList")
    public LayUIResult queryDataXList(HttpServletRequest request,
                                      @RequestParam(value = "page",required = true)Integer pageNum,
                                      @RequestParam(value = "limit",required = true)Integer limit,
                                      @RequestParam(value = "name",required = false)String name,
                                      @RequestParam(value = "startTime",required = false)String startTime,
                                      @RequestParam(value = "stopTime",required = false)String stopTime){
        try{
            //创建参数集合
            Map<String,Object> param = new HashMap<>();
            param.put("name",name);
            param.put("startTime",startTime);
            param.put("stopTime",stopTime);
            //调用pageHelper
            Page<DataX> page = PageHelper.startPage(pageNum,limit);
            List<DataX> list = dataXService.queryList(param);
            layUIResult.setData(list);
            layUIResult.setMsg("配置列表");
            layUIResult.setCode(0);
            layUIResult.setCount((int)page.getTotal());
        }catch (Exception e){
            e.printStackTrace();
        }
        return layUIResult;
    }


    /**
     * 添加DataX的配置信息
     * @param request ：请求对象
     * @return
     */
    @RequestMapping("/addDataXRec")
    public BasicResult addDataXRec(HttpServletRequest request){
        //用于接收写入后的文件名
        String fileJson = null;
        //创建Data X 对象
        DataX dataX = new DataX();
        //创建变量，用于记录是否可以添加
        boolean boo = true;
        //创建变量，接收表达式
        String expression = null;
        try {
            //获取配置的信息
            String name = request.getParameter("name");
            //判断定时表达式
            String expre = request.getParameter("expre");
            //判断是那种表达式
            if (expre != null && !"".equals(expre) && expre.equals("day")) {
                //获取表达式值
                String express = request.getParameter("expressDate");
                //调用转换函数
                expression = DataXUtil.convertExpression(express,1);
            } else {
                //获取表达式值
                String express = request.getParameter("expressTime");
                //判断选中值
                Integer type = expre.equals("min")?2:3;
                //调用转换函数
                expression = DataXUtil.convertExpression(express,type);
            }
            dataX.setExpression(expression);
            dataX.setName(name);
            dataX.setCreatetime(format.format(new Date()));
            //调用函数，获取要写入的内容
            String configJson = disposeJson(request);
            //判断返回是否为空
            if(configJson == null){
                //设置值为false
                boo = false;
            }else{
                //调用函数，将数据写入文件
                fileJson = DataXUtil.writerFile(configJson, "sync");
                dataX.setFilename(fileJson.substring(fileJson.indexOf("sync")));
                dataX.setStatus(1);
            }
            //判断是否通过
            if(boo){
                //添加数据
                Integer count = dataXService.saveDatax(dataX);
                //判端是否为空
                if(count <= 0 ){
                    basicResult.setCode(10001);
                    basicResult.setMsg("配置异常！");
                    //删除文件
                    deleteFile(fileJson);
                }else{
                    //添加/修改 任务
                    boolean result = result(expression,dataX);
                    //判断是否成功
                    if(!result){
                        basicResult.setCode(10001);
                        basicResult.setMsg("配置失败！");
                        //删除文件
                        deleteFile(fileJson);
                        //删除记录
                        dataXService.deleteById(dataX.getId());
                    }else{
                        basicResult.setCode(10000);
                        basicResult.setMsg("配置成功！");
                    }
                }
            }else{
                basicResult.setCode(10001);
                basicResult.setMsg("配置失败！");
                //删除文件
                deleteFile(fileJson);
                //删除记录
                dataXService.deleteById(dataX.getId());
            }
        }catch (Exception e){
            boo = false;
            //删除文件
            deleteFile(fileJson);
            //删除记录
            dataXService.deleteById(dataX.getId());
            e.printStackTrace();
            basicResult.setCode(10001);
            basicResult.setMsg("配置异常！");
        }
        //返回结果
        return basicResult;
    }


    /**
     * 根据文件名，获取文件内容
     * @param fileName ：文件名
     * @return
     */
    @RequestMapping("/getDataXJson")
    public BasicResult getDataXJson(String fileName){
        //异常处理
        try {
            //调用函数，获取文件内容
            String json = DataXUtil.getDataXJson(fileName);
            //判断是否有值
            if(json == null){
                basicResult.setMsg("获取数据为空！");
                basicResult.setCode(10001);
            }else{
                basicResult.setMsg("数据读取成功！");
                basicResult.setCode(10000);
                basicResult.setObj(json);
            }
        }catch (Exception e){
            basicResult.setMsg("获取数据为空！");
            basicResult.setCode(10001);
        }
        //将数据返回
        return basicResult;
    }


    /**
     * 修改配置数据
     * @param request ：请求对象
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @RequestMapping("/modifyDataX")
    public BasicResult modifyDataX(HttpServletRequest request){
        //创建Data X 对象
        DataX dataX = new DataX();
        //创建变量，用于记录是否可以添加
        boolean boo = true;
        //创建变量，接收表达式
        String expression = null;
        //创建变量，接收状态
        Integer status = 2;
        //创建变量，接收文件名
        String fileName = "";
        //创建变量，接收原有文件内容，用于文件回滚
        String content = "";
        try {
            //获取对象ID
            Integer id = Integer.valueOf(request.getParameter("id"));
            //获取对象状态
            status = Integer.valueOf(request.getParameter("status"));
            //获取配置的信息
            String name = request.getParameter("name");
            //获取文件名
            fileName = request.getParameter("fileName");
            //获取创建时间
            String createTime = request.getParameter("createTime");
            //判断定时表达式
            String expre = request.getParameter("expre");
            //判断是那种表达式
            if (expre != null && !"".equals(expre) && expre.equals("day")) {
                //获取表达式值
                String express = request.getParameter("expressDate");
                //调用转换函数
                expression = DataXUtil.convertExpression(express,1);
            } else {
                //获取表达式值
                String express = request.getParameter("expressTime");
                //判断选中值
                Integer type = expre.equals("min")?2:3;
                //调用转换函数
                expression = DataXUtil.convertExpression(express,type);
            }
            dataX.setId(id);
            dataX.setExpression(expression);
            dataX.setName(name);
            dataX.setFilename(fileName);
            //存入对象
            dataX.setCreatetime(createTime);
            dataX.setStatus(status);
            dataX.setModifytime(format.format(new Date()));
            //添加数据
            Integer count = dataXService.updateDatax(dataX);
            //判端是否为空
            if(count <= 0){
                basicResult.setCode(10001);
                basicResult.setMsg("配置异常！");
                boo = false;
            }
            //判断是否通过
            if(boo){
                //调用函数，获取要写入的内容
                String configJson = disposeJson(request);
                //判断返回是否为空
                if(configJson == null){
                    basicResult.setCode(10001);
                    basicResult.setMsg("配置数据为空！");
                    //让数据回滚
                    throw  new Exception("配置数据为空");
                }else{
                    //读取原来的内容 (用于内容回滚)
                    content = DataXUtil.getDataXJson(fileName);
                    //调用函数，进行填写内容
                    boo = DataXUtil.modifyDataXJson(configJson,fileName);
                    //判断是否写入成功
                    if(boo){
                        //判断是否为可用的
                        if(status == 1){
                            //添加/修改 任务
                            boolean result = result(expression,dataX);
                            //判断是否成功
                            if(!result){
                                basicResult.setCode(10001);
                                basicResult.setMsg("配置失败！");
                                //回滚文件数据
                                DataXUtil.modifyDataXJson(content,fileName);
                                //抛出异常，回滚数据库数据
                                throw  new Exception("添加任务失败");
                            }else{
                                basicResult.setCode(10000);
                                basicResult.setMsg("配置成功！");
                            }
                        }else{
                            basicResult.setCode(10000);
                            basicResult.setMsg("配置成功！");
                        }
                    }else{
                        basicResult.setCode(10001);
                        basicResult.setMsg("配置失败！");
                        throw  new Exception("写入文件失败");
                    }
                }
            }else{
                basicResult.setCode(10001);
                basicResult.setMsg("配置写入失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            boo = false;
            basicResult.setCode(10001);
            basicResult.setMsg("修改配置异常！");
            //回滚文件数据
            DataXUtil.modifyDataXJson(content,fileName);
            //不影响数据正常混滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        //返回结果
        return basicResult;
    }





    /**
     * 弃用数据
     * @param request ：请求对象
     * @param fileName ：文件名
     * @param id ：ID
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @RequestMapping("/removeDataX")
    public BasicResult removeDataX(HttpServletRequest request,
                                   @RequestParam(value = "fileName",required = true)String fileName,
                                   @RequestParam(value = "id",required = true)Integer id,
                                   @RequestParam(value = "expression",required = true)String expression,
                                   @RequestParam(value = "status",required = true)Integer status){
        //创建对象
        DataX obj = null;
        try {
            //调用修改文件名接口
            String fileNames = null;
            //创建对象
            DataX dataX = new DataX();
            dataX.setId(id);
            dataX.setStatus(status);
            dataX.setExpression(expression);
            //判断是弃用，还是启用
            if(status == 1){
                fileNames = DataXUtil.modifyFileName(fileName,"sync");
                dataX.setFilename(fileNames.substring(fileNames.indexOf("sync")));
            }else{
                fileNames = DataXUtil.modifyFileName(fileName,"not");
                dataX.setFilename(fileNames.substring(fileNames.indexOf("not")));
            }
            //判断文件是否写入正常
            if(fileNames != null){
                //创建Map集合，存储参数
                Map<String,Object> param = new HashMap<>();
                //将参数存入集合
                param.put("id",id);
                param.put("status",status);
                param.put("filename",dataX.getFilename());
                //调用接口，修改数据
                Integer count = dataXService.updateDataXByStatus(param);
                //判断是否成功
                if(count <= 0){
                    //判断是弃用还是启用
                    if(status == 1){
                        DataXUtil.modifyFileName(fileName,"not");
                        basicResult.setCode(10001);
                        basicResult.setMsg("启用失败！");
                    }else{
                        DataXUtil.modifyFileName(fileName,"sync");
                        basicResult.setCode(10001);
                        basicResult.setMsg("弃用失败！");
                    }
                }else{
                    //判断是弃用还是启用
                    if(status == 1){
                        //判断任务是否添加成功
                        if(result(expression,obj)){
                            basicResult.setCode(10000);
                            basicResult.setMsg("启用成功！");
                        }else{
                            DataXUtil.modifyFileName(fileName,"not");
                            basicResult.setCode(10001);
                            basicResult.setMsg("启用失败！");
                            throw new Exception("启用任务添加失败！");
                        }
                    }else{
                        //判断任务是否添加成功
                        if(result(expression,obj)){
                            basicResult.setCode(10000);
                            basicResult.setMsg("弃用成功！");
                        }else{
                            DataXUtil.modifyFileName(fileName,"sync");
                            basicResult.setCode(10001);
                            basicResult.setMsg("弃用失败！");
                            throw new Exception("弃用任务添加失败！");
                        }
                    }
                }
            }else{   //文件名写入错误
                basicResult.setCode(10001);
                basicResult.setMsg("文件写入错误！");
            }
        }catch (Exception e){
            e.printStackTrace();
            basicResult.setCode(10001);
            basicResult.setMsg("系统异常！");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return basicResult;
    }


    /**
     * 处理客户端上传的参数
     * @return ：根据参数拼接好的数据
     */
    public String disposeJson(HttpServletRequest request){
        //创建对象字符串，接收最终数据
        String configJson = null;
        try{
            //数据源的数据库名
            String outDatabaseName = request.getParameter("outDatabaseName");
            //接收数据的数据库名
            String inDatabaseName = request.getParameter("inDatabaseName");
            //数据源的IP地址
            String outIPAddress = request.getParameter("outIPAddress");
            //数据源端口
            String outPort = request.getParameter("outPort");
            //接收数据的IP地址
            String inIPAddress = request.getParameter("inIPAddress");
            //接收数据的端口
            String inPort = request.getParameter("inPort");
            //数据源用户名
            String outUserName = request.getParameter("outUserName");
            //数据源密码
            String outPassword = request.getParameter("outPassword");
            //接受同步用户名
            String inUserName = request.getParameter("inUserName");
            //接受同步密码
            String inPassword = request.getParameter("inPassword");
            //写入模型
            String writeMode = request.getParameter("writeMode");
            //通道数
            Integer channelCount = Integer.valueOf(request.getParameter("channelCount"));
            //同步前 被同步表执行的SQL
            String preSql = request.getParameter("perSql");
            //同步后 被同步表执行的SQL
            String postSql = request.getParameter("postSql");
            //获取同步方式
            String syncMethod = request.getParameter("syncMethod");
            //接收的数据列
            String inColumn = request.getParameter("inColumn");
            String[] inColumnArr = inColumn.split(",");
            //接收的数据表
            String inTable = request.getParameter("inTable");
            String[] inTableArr = inTable.split(",");
            //切片 同步
            String splitPk = request.getParameter("splitPk");
            //数据源连接串
            String outJdbcUrl = DataXUtil.appendConnectStr(outIPAddress, outPort, outDatabaseName);
            //接收数据的连接串
            String inJdbcUrl = DataXUtil.appendConnectStr(inIPAddress, inPort, inDatabaseName);
            //列同步
            if (syncMethod != null && !"".equals(syncMethod) && syncMethod.equals("columnSync")) {
                //数据源列
                String outColumn = request.getParameter("outColumn");
                String[] outColumnArr = outColumn.split(",");
                //数据源表
                String outTable = request.getParameter("outTable");
                String[] outTableArr = outTable.split(",");
                //调用生成配置文件
                configJson = DataXUtil.createJson(inJdbcUrl, new String[]{outJdbcUrl}, inUserName,
                        inPassword, outUserName, outPassword, inColumnArr, outColumnArr, inTableArr,
                        outTableArr, splitPk, "mysqlreader", "mysqlwriter", channelCount, writeMode,
                        null, preSql, postSql);
            }//SQL同步
            else {
                //查询SQL
                String querySql = request.getParameter("querySql");
                //调用生成配置文件
                configJson = DataXUtil.createJson(inJdbcUrl, new String[]{outJdbcUrl}, inUserName,
                        inPassword, outUserName, outPassword, inColumnArr, null, inTableArr,
                        null, splitPk, "mysqlreader", "mysqlwriter", channelCount, writeMode,
                        querySql, preSql, postSql);
            }
        }catch (Exception e){
            e.printStackTrace();
            configJson = null;
        }
        return configJson;
    }




    /**
     * 用于处理任务的添加、修改
     * @param expression ：表达式
     * @param dataX ：对象
     * @return
     */
    public boolean result(String expression,DataX dataX){
        //判断是否已存在，相同的表达式(同样的定时时间)
        boolean bool = QuartzManager.futureMap.containsKey(expression);
        try {
            //判断
            if(bool){
                //获取数据库中的数据 [ 重新获取的数据 ]
                List<DataX> list = dataXService.queryDataXByExpress(expression);
                //获取对应的任务
                ScheduledFuture<?> future = QuartzManager.futureMap.get(expression);
                //停止任务
                future.cancel(true);
                //判断是否数据库中 同表达式 还有任务
                if(list != null && list.size() > 0){
                    //调用添加函数接口
                    QuartzManager.addJob(expression,list);
                }else{
                    //移除MAP中失效的任务
                    QuartzManager.futureMap.remove(expression);
                }
            }else{
                //调用添加任务的函数
                QuartzManager.addJob(expression,new ArrayList<DataX>(){{ add(dataX); }});
            }
            bool = true;
        }catch (Exception e){
            bool = false;
            e.printStackTrace();
        }
        return bool;
    }


    /**
     * 根据文件名，删除文件
     * @param fileName ：文件名
     * @return
     */
    public boolean deleteFile(String fileName){
        //创建变量，接收结果
        boolean boo = false;
        //异常处理
        try {
            //根据地址，获取文件
            File file = new File(fileName);
            //判断是否存在
            if(file.exists()){
                boo = file.delete();
            }
        }catch (Exception e){
            boo = false;
        }
        return boo;
    }

}
