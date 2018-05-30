package com.lazada.microservice.util;

import java.sql.*;

public class ValidationDataXConfiguration {


    /**
     * 校验DataX配置的数据同步，是否正确
     * @param inUrl ：接收数据库连接字符串
     * @param outUrl ：同步数据库连接字符串
     * @param inUserName ：接收的用户名
     * @param outUserName ：同步的用户名
     * @param inPassword ：接收的密码
     * @param outPassword ：同步的密码
     * @param inColumn ：接收的列名
     * @param outColumn ：同步的列名
     * @param querySql ：查询SQL
     * @param inTableName ：接收的表名
     * @param outTableName ：同步的表名
     * @return
     */
    public static boolean validationSQL(String inUrl,String outUrl,
                                        String inUserName,String outUserName,
                                        String inPassword,String outPassword,
                                        String inColumn,String outColumn,
                                        String querySql,String inTableName,
                                        String outTableName){
        //创建数据库连接对象
        Connection outConnection = null;
        Connection inConnection = null;
        //创建同步执行对象
        PreparedStatement outStatement = null;
        //创建接收执行对象
        PreparedStatement inStatement = null;
        //创建结果集对象
        ResultSet resultSet = null;
        //异常处理
        try {
            //加载驱动
            Class.forName("com.mysql.jdbc.Driver");//加载驱动类
            //创建同步连接对象
            outConnection = DriverManager.getConnection(outUrl,outUserName,outPassword);
            //判断是否执行 SQL 方式，还是 列名方式
            if(querySql != null && !"".equals(querySql)){
                //判断是否存在分页
                if(querySql.indexOf("limit") == -1){
                    //判断是否有 分号结尾
                    if(querySql.indexOf(";") != -1){
                        //去除分号
                        String sql = querySql.substring(0,querySql.length()-1);
                        //拼接分页处理
                        querySql = sql + "  limit 0,1 ;";
                    }else{
                        //拼接分页处理
                        querySql = querySql + "  limit 0,1 ;";
                    }
                }
                //将SQL存入执行器中
                outStatement = outConnection.prepareStatement(querySql);
            } //列名方式
            else{
                //拼接SQL
                String definedSQL = "select "+outColumn + " from " + outTableName +" limit 0,1";
                //将SQL存入执行器中
                outStatement = outConnection.prepareStatement(definedSQL);
            }
            //执行SQL，获取结果集
            resultSet = outStatement.executeQuery();

            //创建接收数据库连接对象
            inConnection = DriverManager.getConnection(inUrl,inUserName,inPassword);

            //执行接收SQL


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                //判断关闭
                if(resultSet != null){
                    resultSet.close();
                }
                //判断关闭
                if(outStatement != null){
                    outStatement.close();
                }
                //判断关闭
                if(outConnection != null){
                    outConnection.close();
                }
                //判断关闭
                if(inStatement != null){
                    inStatement.close();
                }
                //判断关闭
                if(inConnection != null){
                    inConnection.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }



        return true;
    }



    public static String disposeSQL(String column){


        return "";
    }




}
