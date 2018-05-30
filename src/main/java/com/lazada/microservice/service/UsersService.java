package com.lazada.microservice.service;

import com.lazada.microservice.model.Users;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface UsersService {



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
     * 新增用户
     * @param users ：用户对象
     * @return
     */
    Integer addUsers(Users users);

    /**
     * 更新用户信息
     * @param users ：用户对象
     * @return
     */
    Integer updateUsers(Users users);


    /**
     * 单个，批量删除用户信息
     * @param id ：用户ID字符串
     * @return
     */
    Integer deleteUsers(@Param("idStr")String idStr);

}
