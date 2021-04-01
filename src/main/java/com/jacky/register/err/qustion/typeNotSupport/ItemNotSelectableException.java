package com.jacky.register.err.qustion.typeNotSupport;

import com.jacky.register.err.BaseException;
import com.jacky.register.models.database.quetionail.subItems.ItemSort;

public class ItemNotSelectableException extends BaseException {
    final static private int errorCode=303;
    public ItemNotSelectableException(ItemSort item){
        super(errorCode,format(item));
    }
    static String format(ItemSort item){
        return
                String.format(
                        "Item<ID:%s | PK:%s | Type:%s>",
                        item.sortIndex,item.item.id,item.item.type
                );
    }
}
