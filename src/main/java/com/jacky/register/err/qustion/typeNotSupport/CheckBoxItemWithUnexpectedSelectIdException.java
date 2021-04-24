package com.jacky.register.err.qustion.typeNotSupport;

import com.jacky.register.err.BaseException;
import com.jacky.register.models.database.quetionail.subItems.ItemSort;

public class CheckBoxItemWithUnexpectedSelectIdException extends BaseException {
    private final static int errorCode=305;
    public CheckBoxItemWithUnexpectedSelectIdException(ItemSort sort){
        super(errorCode,
                String.format(
                        "Item<Info:`%s`| ID:%s> CHECK BOX Require Select Index 0<False> OR 1<True>",
                        sort.item.data,sort.sortIndex
                ));
    }
}
