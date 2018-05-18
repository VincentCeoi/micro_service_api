package com.lazada.microservice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class Users implements Serializable {

    private Integer id;

    private String name;

    private String password;

    private Integer status;

    private Integer roleId;

    private String createTime;


}
