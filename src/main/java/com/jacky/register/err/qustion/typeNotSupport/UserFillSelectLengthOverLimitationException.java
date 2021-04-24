package com.jacky.register.err.qustion.typeNotSupport;

import com.jacky.register.err.BaseException;
import com.jacky.register.models.database.quetionail.choices.SelectSort;
import com.jacky.register.models.database.quetionail.subItems.ItemSort;

public class UserFillSelectLengthOverLimitationException extends BaseException {
    private final static int errorCode=306;
    public UserFillSelectLengthOverLimitationException(ItemSort itemSort, SelectSort selectSort,int fillSize){
        super(errorCode,
                String.format(
                        "Item<Data:`%s`|ID:%s> Select<Data:`%s`|ID:%s> User FillSize<%s> Over Limitation<32>",
                        itemSort.item.data,itemSort.sortIndex,selectSort.select.information,selectSort.sortIndex,
                        fillSize
                ));
    }
}
