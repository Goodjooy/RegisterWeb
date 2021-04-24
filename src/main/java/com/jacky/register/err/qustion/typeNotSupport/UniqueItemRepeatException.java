package com.jacky.register.err.qustion.typeNotSupport;

import com.jacky.register.err.BaseException;
import com.jacky.register.models.database.quetionail.subItems.ItemSort;

public class UniqueItemRepeatException extends BaseException {
    private static final int errorCode=306;
    public UniqueItemRepeatException(ItemSort itemSort,String collect){
        super(errorCode,
                String.format(
                        "Item<Data:`%s`|ID:%s> Require Unique But Same Collect Item<`%s`> Found",
                        itemSort.item.data,itemSort.sortIndex,collect
                ));
    }

}
