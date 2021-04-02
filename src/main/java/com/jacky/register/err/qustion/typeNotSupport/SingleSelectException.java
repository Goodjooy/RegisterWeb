package com.jacky.register.err.qustion.typeNotSupport;

import com.jacky.register.err.BaseException;
import com.jacky.register.models.database.quetionail.subItems.ItemSort;

public class SingleSelectException extends BaseException {
    final static private int errorCode = 302;

    public SingleSelectException(ItemSort item) {
        super(errorCode,informationFormat(item));
    }

    static String informationFormat(ItemSort item) {
        return
                String.format(
                        "Item<ID:%s | PK:%s | Type:%s> Not Support Multi-Selection"
                        , item.sortIndex, item.item.id, item.item.type
                );
    }
}
