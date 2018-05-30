package com.lazada.microservice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
@Getter
@Setter
@ToString
@Table(name = "pfl_linehaul")
public class Platform {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    private String name;

    @Column(name = "trading_name")
    private String tradingName;

    @Column(name = "linehaul_code")
    private String linehaulCode;

    @Column(name = "api_key")
    private String apiKey;

    private Integer status;
}