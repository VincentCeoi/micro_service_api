package com.lazada.microservice.request;

import lombok.Data;

/**
 * 添加第三方平台,请求参数
 * Created by Jack liu on 2018/5/21.
 */
@Data
public class PlatformReq {

//    @NonNull("名称不能为空")
    private String name;

    private String tradingName;

    private String linehaulCode;

    private String apiKey;

    private Integer status;
}
