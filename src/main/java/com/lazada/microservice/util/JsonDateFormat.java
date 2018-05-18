package com.lazada.microservice.util;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/12/28 0028.
 */
public class JsonDateFormat implements JsonValueProcessor {

    //时间格式
    private String format = "yyyy-MM-dd";


    public JsonDateFormat() {
    }

    public JsonDateFormat(String format) {
        this.format = format;
    }

    public Object processArrayValue(Object value, JsonConfig jsonConfig) {
        String[] obj = {};
        if (value instanceof Date[]) {
            SimpleDateFormat sf = new SimpleDateFormat(format);
            Date[] dates = (Date[]) value;
            obj = new String[dates.length];
            for (int i = 0; i < dates.length; i++) {
                obj[i] = sf.format(dates[i]);
            }
        }
        return obj;
    }

    public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
        if (value instanceof Date) {
            String str = new SimpleDateFormat(format).format((Date) value);
            return str;
        }
        return value;
    }
}