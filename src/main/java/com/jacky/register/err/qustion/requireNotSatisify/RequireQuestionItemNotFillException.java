package com.jacky.register.err.qustion.requireNotSatisify;

import com.jacky.register.err.BaseException;
import com.jacky.register.models.database.quetionail.subItems.ItemSort;

import java.util.Base64;

public class RequireQuestionItemNotFillException extends BaseException {
    private final static int errorCode=201;

    public RequireQuestionItemNotFillException(long itemCount){
        super(errorCode,informationFormat(itemCount));
    }

    public RequireQuestionItemNotFillException(ItemSort item) {
            super(errorCode,informationFormat(item));
    }

    static String informationFormat(long item){
        return
                String.format(
                        "%s More Item Require But Not Satisfy",item
                );
    }
    static String informationFormat(ItemSort item){
        return
                String.format(
                        "Item<ID:%s|Pk:%s|Type:%s> Require But Not Satisfy",
                        item.sortIndex,item.item.id,item.item.type
                );
    }
}
