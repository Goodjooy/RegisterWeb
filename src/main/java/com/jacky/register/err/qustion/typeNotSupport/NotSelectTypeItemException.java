package com.jacky.register.err.qustion.typeNotSupport;

import com.jacky.register.err.BaseException;
import com.jacky.register.models.database.quetionail.ItemType;
import com.jacky.register.models.database.quetionail.Questionable;
import com.jacky.register.models.database.quetionail.subItems.ItemSort;

/**
 * {@code NotSelectTypeItemException}
 * <p>
 * 在试图对非
 * <ul>
 *  <li>`MULTI_CHOICE`多重选择</li>
 *  <li>`SINGLE_CHOICE`单独选择</li>
 * </ul>
 * 抛出
 * </p>
 * <p>
 * 异常码: {@code 301}
 */
public class NotSelectTypeItemException extends BaseException {
    private static final int errorCode=301;
    public NotSelectTypeItemException(ItemSort item) {
        super(errorCode,informationFormat(item));
    }
    static String informationFormat(ItemSort item){
        return
                String.format(
                        "Item<ID:%s| Data:`%s`> Type<TYPE:%s> Can NOT Add Select",
                        item.sortIndex,item.item.data,item.item.type
                );
    }
}
