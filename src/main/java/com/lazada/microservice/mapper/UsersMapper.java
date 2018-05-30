package com.lazada.microservice.mapper;

import com.lazada.microservice.core.Mapper;
import com.lazada.microservice.model.Users;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface UsersMapper  extends Mapper<Users> {


    /**
     * 根据用户名，获取用户对象
     * @param name ：用户名
     * @return
     */
    Users queryUsersByName(@Param("name")String name);


    /**
     * 获取用户列表
     * @param param ：参数集合
     * @return
     */
    List<Users> queryUsersList(Map<String,Object> param);


    /**
     * 单个，批量删除用户信息
     * @param id ：用户ID字符串
     * @return
     */
    Integer deleteUsers(@Param("idStr")String id);

}
