package com.jacky.register.dataHandle.dataCheck.dataGeter;

import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.function.Function;


public class SpecialTransformer<TARGET> implements Function<List<DataResource>,TARGET>{
    //目标字段名称
    private String targetFieldName;


    /**
     * Applies this function to the given argument.
     *
     * @param dataResources the function argument
     * @return the function result
     */
    @Override
    public TARGET apply(List<DataResource> dataResources) {
        // TODO: 2021/4/26 好耶
        return null;
    }

    TARGET apply(){
        // TODO: 2021/4/26 自动参数获取
        return null;
    }
}
