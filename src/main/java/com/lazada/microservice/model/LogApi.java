package com.lazada.microservice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
@Getter
@Setter
@ToString
@Table(name = "log_api")
public class LogApi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "created_at")
    private Date createdAt;

    private String platform;

    private String url;

    @Column(name = "request_param")
    private String requestParam;

    private String response;

    @Column(name = "rst_code")
    private Integer rstCode;

    @Column(name = "rst_msg")
    private String rstMsg;

}