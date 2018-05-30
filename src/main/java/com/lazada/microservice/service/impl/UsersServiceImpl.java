package com.lazada.microservice.service.impl;

import com.lazada.microservice.mapper.UsersMapper;
import com.lazada.microservice.model.Users;
import com.lazada.microservice.service.UsersService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class UsersServiceImpl implements UsersService {


    @Resource
    private UsersMapper usersMapper;


    @Override
    public Users queryUsersByName(String name) {
        return usersMapper.queryUsersByName(name);
    }

    @Override
    public List<Users> queryUsersList(Map<String, Object> param) {
        return usersMapper.queryUsersList(param);
    }


    @Override
    public Integer addUsers(Users users) {
        return usersMapper.insert(users);
    }

    @Override
    public Integer updateUsers(Users users) {
        return usersMapper.updateByPrimaryKey(users);
    }

    @Override
    public Integer deleteUsers(String id) {
        return usersMapper.deleteUsers(id);
    }
}
