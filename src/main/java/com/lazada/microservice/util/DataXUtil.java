package com.lazada.microservice.util;

import com.alibaba.fastjson.JSON;
import net.sf.json.JSONObject;

import java.io.*;
import java.text.SimpleDateFormat;

public class DataXUtil {

    /**
     * //延迟0分钟开始执行
     */
    public static Long initDelay = (long)0 * 1000;
    /**
     * //json文件夹地址
     */
    public static String jsonPath;
    /**
     * //datax的python文件地址
     */
    public static String dataxPath;

    /**
     * 时间格式对象
     */
    public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public static void main(String[] args) {
        //定时任务
        /*ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        //添加任务，执行
        service.scheduleAtFixedRate(new Runnable(){

            @Override
            public void run() {

            }

        }, initDelay, 1, TimeUnit.MILLISECONDS);*/
        /*exeDatax();*/

    }

    /**
     * 执行批量同步 或是 指定单个同步
     * @param fileName ：需要同步的文件名
     */
    public  void executeDataX(String fileName){
        try {
            System.out.println("------------------start----------------------");
            //判断是指定单个执行，还是批量执行指定文件夹下的
            if(fileName != null && !"".equals(fileName)){
                executeSyc(fileName);
            }else{
                //读取文件
                String[] str = getFileName(jsonPath);
                //遍历数组
                for (String name : str) {
                    executeSyc(name);
                }
            }
            System.out.println("----------------end------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 执行同步
     * @param name ：文件名
     */
    public static void executeSyc(String name){
        try {
            //拼接执行命令
            String windowcmd = "python "+dataxPath+" "+jsonPath+"/"+name;
            //输出执行命令
            System.out.println(windowcmd);
            //调用执行exe程序
            Process pr = Runtime.getRuntime().exec(windowcmd);
            //获取读取流
            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            pr.waitFor();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 获取文件夹下所有 符合同步规则 json 文件名
     * @param path ：指定获取的文件的目录
     * @return ：返回多个文件名
     */
    public  String[] getFileName(String path) {
        File file = new File(path);
        //获取文件名
        String[] fileName = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                //sync 标识是正常使用的，如果不是就是弃用
                if (name.endsWith(".json") && name.indexOf("not") == -1) {
                    return true;
                }
                return false;
            }
        });
        return fileName;
    }


    /**
     * 拼接DataX 执行数据
     * @param inJdbcUrl ：接受同步的数据库连接（单个）
     * @param outJdbcUrl ：获取同步的数据库连接(可以多个)
     * @param inUserName ：接受的数据库用户名
     * @param inPassword ：接受的数据库密码
     * @param outUserName ：获取同步数据的用户名
     * @param outPassword ：获取同步数据的密码
     * @param inColumn ：接受的列名
     * @param outColumn ：获取的列名
     * @param inTable ：接受的表名
     * @param outTable ：获取的表名
     * @param splitPk ：数据同步切片 （目前splitPk仅支持整形数据切分，不支持浮点、字符串、日期等其他类型）
     * @param readerName ：读取的名称
     * @param writerName ：写入的名称
     * @param channelCount ：通道数量
     * @param writerMode ：写入模型 （insert/replace/update）
     * @param querySql ：读取查询SQL （reader）
     * @param preSql ：同步前执行的SQL （write）
     * @param postSql ：同步后执行的SQL （write）
     * @return
     */
    public static String createJson(String inJdbcUrl,String[] outJdbcUrl,String inUserName,String inPassword,
                                    String outUserName,String outPassword,
                                    String[] inColumn,String[] outColumn,String[] inTable,
                                    String[] outTable,String splitPk,String readerName,String writerName,
                                    Integer channelCount,String writerMode,String querySql,String preSql,
                                    String postSql){
        //创建变量，用户判断是否拼接配置
        boolean boo = true;
        //创建对象,拼接数据
        StringBuffer job = new StringBuffer("{\"job\": {" +
                "        \"setting\": {" +
                "            \"speed\": {" +
                "                 \"channel\": "+ channelCount +
                "            }," +
                "            \"errorLimit\": {" +
                "                \"record\": 0," +
                "                \"percentage\": 0.02" +
                "            }" +
                "        }," +
                "        \"content\": [" +
                "            {" +
                "                \"reader\": {" +
                "                    \"name\": \""+readerName+"\"," +
                "                    \"parameter\": {" +
                "                        \"username\": \""+outUserName+"\"," +
                "                        \"password\": \""+outPassword+"\",");
                //判断column
                if(outColumn != null && outColumn.length > 0){
                    job.append("\"column\": [");
                    //遍历数据
                    for(int i =0; i < outColumn.length;i++){
                        //最后一个 ：不要逗号
                        if(i + 1 == outColumn.length){
                            job.append("\""+outColumn[i]+"\"");
                        }else{
                            job.append("\""+outColumn[i]+"\",");
                        }
                    }
                    job.append("],");
                    //标识不可行
                    boo = false;
                }
                //继续拼接
                job.append("\"splitPk\": \""+splitPk+"\"," +
                "                        \"connection\": [" +
                "                            {");
                //判断查询SQL是否为空 并且前面还没有设置列头
                if(querySql != null && !"".equals(querySql) && boo){
                    //使用SQL查询
                    job.append("\"querySql\":[\""+querySql+"\"],");
                }else{
                    job.append("\"table\": [");
                    //遍历表
                    for(int i =0; i < outTable.length;i++){
                        //最后一个 ：不要逗号
                        if(i + 1 == outTable.length){
                            job.append("\""+outTable[i]+"\"");
                        }else{
                            job.append("\""+outTable[i]+"\",");
                        }
                    }
                    //继续拼接
                    job.append("],");
                }
                //继续拼接
                job.append("\"jdbcUrl\": [");
                //遍历数据库连接
                for(int i =0; i < outJdbcUrl.length;i++){
                    //最后一个 ：不要逗号
                    if(i + 1 == outJdbcUrl.length){
                        job.append("\""+outJdbcUrl[i]+"\"");
                    }else{
                        job.append("\""+outJdbcUrl[i]+"\",");
                    }
                }
                //继续拼接
                job.append("] } ] } },\"writer\":{");
                job.append("\"name\":\""+writerName+"\",\"parameter\": {");
                job.append("\"writeMode\":\""+writerMode+"\",");
                job.append("\"username\":\""+inUserName+"\",");
                job.append("\"password\":\""+inPassword+"\",");
                //判断column
                if(inColumn != null && inColumn.length > 0){
                    job.append("\"column\": [");
                    //遍历数据
                    for(int i =0; i < inColumn.length;i++){
                        //最后一个 ：不要逗号
                        if(i + 1 == inColumn.length){
                            job.append("\""+inColumn[i]+"\"");
                        }else{
                            job.append("\""+inColumn[i]+"\",");
                        }
                    }
                    job.append("],");
                }
                //判断同步前需要执行的SQL是否为空
                if(preSql != null && !"".equals(preSql)){
                    job.append("\"preSql\":[\""+preSql+"\"],");
                }
                //判断同步后需要执行的SQL是否为空
                if(postSql != null && !"".equals(postSql)){
                    job.append("\"postSql\":[\""+postSql+"\"],");
                }
                job.append("\"connection\": [ {");
                job.append("\"jdbcUrl\": \""+inJdbcUrl+"\"");
                //继续拼接
                job.append(",\"table\": [");
                //遍历表
                for(int i =0; i < inTable.length;i++){
                    //最后一个 ：不要逗号
                    if(i + 1 == inTable.length){
                        job.append("\""+inTable[i]+"\"");
                    }else{
                        job.append("\""+inTable[i]+"\",");
                    }
                }
                job.append("] } ] } } } ] } }");
                //将数据输出控制台
        System.out.println("数据为：\n"+job.toString());
        return job.toString();
    }






    /**
     * 将数据写入文件
     * @param content ： 要写入的内容
     */
    public  static  String writerFile(String content,String fileName){
        //获取当前字符串
        long systemTime = System.currentTimeMillis();
        //将时间戳转换成日期
        String date = new SimpleDateFormat("yyyy-MM-dd").format(systemTime);
        //创建变量，接收文件名
        String newFileName = null;
        try {
            //进行创建文件
            FileOutputStream out = new FileOutputStream(jsonPath+"/"+fileName+"-"+date+systemTime+".json"); // 输出文件路径
            //使用输出流写文件
            out.write(content.getBytes());
            //关闭流处理
            out.close();
            //接收拼接的文件地址
            newFileName = jsonPath+"/"+fileName+"-"+date+systemTime+".json";
        } catch (Exception e) {
            e.printStackTrace();
            newFileName = null;
        }
        return newFileName;
    }


    /**
     * 修改文件名
     * @param fileName ：地址 + 文件名
     */
    public static String modifyFileName(String fileName,String name){
        //根据URL，获取文件
        File file=new File(jsonPath+"/"+fileName);   //指定文件名及路径
        //创建变量，接收文件名
        String fileNameModify = null;
        //判断文件，是否存在
        if(!file.exists()){
            //不用处理
            return null;
        }else{
            //获取文件名
            String  filename= name + fileName.substring(fileName.indexOf("-"));
            //改名后的文件名
            fileNameModify = jsonPath+"/"+filename;
            //改名
            file.renameTo(new  File(fileNameModify));
        }
        //返回文件名
        return fileNameModify;
    }

    /**
     * 拼接MySQL连接字符串
     * @param ip ：IP
     * @param port ：端口
     * @param databaseName ：数据库名
     * @return
     */
    public static String appendConnectStr(String ip,String port,String databaseName){
        //创建变量，用于拼接字符串
        StringBuffer buf = new StringBuffer("jdbc:mysql://");
        //拼接IP地址
        buf.append(ip+":"+port+"/"+databaseName);
        //拼接后缀
        buf.append("?useUnicode=true&characterEncoding=utf8&characterSetResults=utf8");
        //返回拼接好的连接串
        return buf.toString();
    }


    /**
     * 将客户端传入的时间，进行转换 定时任务的 表达式
     * @param express ：时间
     * @param flag ：标识 1 - 时间 2 - 分钟 3 - 秒
     * @return
     */
    public static String  convertExpression(String express,Integer flag){
        String expressionStr = null;
        try{
            //判断是时间 还是 日期
            if(flag.equals(1)){  //时间
                //将时间进行拆分
                String hour = express.substring(0,2);
                //拆分分钟
                String min = express.substring(3,5);
                //拆分秒
                String mis = express.substring(6);
                //将时间拼成 表达式
                StringBuffer expression = new StringBuffer(mis);
                //拼接 空格
                expression.append(" ");
                //拼接 分钟
                expression.append(min);
                //拼接 空格
                expression.append(" ");
                //拼接 小时
                expression.append(hour);
                // 星期 月 日
                expression.append(" * * ?");
                //打印表达式
                System.out.println(expression.toString());
                expressionStr = expression.toString();
            }else if(flag.equals(2)){  //分钟
                //拼接表达式
                String expression = "0 */"+express+" * * * ?";
                //打印表达式
                System.out.println(expression);
                expressionStr = expression;
            }else{   //秒
                //拼接表达式
                String expression = "*/"+express+" * * * * ?";
                //打印表达式
                System.out.println(expression);
                expressionStr = expression;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return expressionStr;
    }


    /**
     * 写入内容到文件中
     * @param jobStr ：写入内容
     * @param fileName ：文件名
     * @return
     */
    public static boolean modifyDataXJson(String jobStr,String fileName){
        //异常处理
        try {
            //根据文件名，获取文件
            File file = new File(jsonPath+"/"+fileName);
            //创建写入对象
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            //将内容写入文件
            writer.write(jobStr);
            //关闭写入对象
            writer.close();
        }catch (Exception e){
            return false;
        }
        return true;
    }


    /**
     * 读取文件内容
     * @param fileName ：文件名
     * @return
     */
    public static String getDataXJson(String fileName){
        //根据文件名，获取文件
        File file = new File(jsonPath+"/"+fileName);
        BufferedReader reader = null;
        //创建JSON对象，接收数据
        JSONObject json = null;
        //创建字符串处理对象
        StringBuffer buffer = new StringBuffer();
        //异常处理
        try {
            //创建字符串，接收读取内容
            String content = "";
            //创建读取对象
            reader = new BufferedReader(new FileReader(file));
            //迭代读取内容
            while((content = reader.readLine()) != null){
                //拼接读取内容
                buffer.append(content);
            }
            reader.close();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return buffer.toString();
    }





}
