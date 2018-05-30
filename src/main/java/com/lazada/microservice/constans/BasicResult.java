package com.lazada.microservice.constans;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class BasicResult {

    /**
     * 返回的编码
     */
    private Integer code;
    /**
     * 返回说明信息
     */
    private String msg;
    /**
     * 携带的对象
     */
    private Object obj;
    /**
     * 携带的集合
     */
    private List<?> list;

    public BasicResult success(String msg) {
        this.code=10000;
        this.msg = msg;
        return this;
    }

    public BasicResult fail(String msg) {
        this.code=10001;
        this.msg = msg;
        return this;
    }

}
