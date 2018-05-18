package com.lazada.microservice.constans;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;



@Setter
@Getter
@ToString
@Component
public class LayUIResult implements Serializable {


    private  Integer code;


    private String msg;


    private Integer count;


    private List data;

}
