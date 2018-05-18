package com.lazada.microservice.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
public class DataX implements Serializable {

    private Integer id;

    private String name;

    private String filename;

    private Integer status;

    private String expression;

    private String createtime;

    private String modifytime;


}
