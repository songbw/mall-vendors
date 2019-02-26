package com.software.seller.util;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.JSONObject;
import java.util.*;

public class JSONResult{
    public static String fillResultString(Integer code, String msg, Object data){
        Map < String , Object > jsonMap = new HashMap< String , Object>();
        jsonMap.put("code",code);
        jsonMap.put("msg",msg);
        jsonMap.put("result",data);

        return JSONObject.toJSONString(jsonMap,SerializerFeature.WriteMapNullValue);
    }
}

