package com.jacky.register.err.qustion.typeNotSupport;

import com.jacky.register.err.BaseException;
import com.jacky.register.models.database.quetionail.subItems.ItemSort;

public class ItemDataLengthOverLimitationException extends BaseException {
    private final static int errorCode=304;
    public ItemDataLengthOverLimitationException(int maxLen, int provideLen, ItemSort item){
        super(
                errorCode,
                String.format(
                        "Item<Data:`%s` | ID:%s> Data<MaxLen:%s | provideLen:%s> Over Limitation",
                        item.item.data,item.sortIndex,maxLen,provideLen
                )
        );
    }
}
