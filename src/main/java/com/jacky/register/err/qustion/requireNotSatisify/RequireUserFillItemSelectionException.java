package com.jacky.register.err.qustion.requireNotSatisify;

import com.jacky.register.err.BaseException;
import com.jacky.register.models.database.quetionail.choices.SelectSort;
import com.jacky.register.models.database.quetionail.subItems.ItemSort;

public class RequireUserFillItemSelectionException extends BaseException {
    private final static int errorCode=202;
    public RequireUserFillItemSelectionException(ItemSort item, SelectSort select){
        super(errorCode,
                String.format(
                        "Item<Data: `%s`| ID:%s> Select<Data:`%s`|ID:%s> Require User Fill Information",
                        item.item.data,item.sortIndex,select.select.information,select.sortIndex
                ));
    }
}
