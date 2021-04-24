package com.jacky.register.err.qustion.typeNotSupport;

import com.jacky.register.err.BaseException;
import com.jacky.register.models.database.quetionail.ItemType;

public class ItemTypePatternNotMatchException extends BaseException {
    private final static int errorCode=303;
    public ItemTypePatternNotMatchException(String itemInfo, ItemType type){
        super(errorCode,
                String.format(
                        "Item Collection Data<`%s`> NOT Match Item Type<%s> Pattern",
                        itemInfo,type.toString()
                )
        );
    }
}
