package com.lazada.microservice.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "province_id")
    private Integer provinceId;

    @Column(name = "city_name")
    private String cityName;

    private String description;

    @Column(name = "create_time")
    private Date createTime;

}