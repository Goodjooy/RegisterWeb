package com.jacky.register.err.qustion.notFound;

import com.jacky.register.err.BaseException;
import com.jacky.register.models.database.quetionail.choices.SubItemSelect;
import com.jacky.register.models.database.quetionail.subItems.ItemSort;

/**
 * {@code ItemSelectNotFoundException}
 * <p>
 * 在
 *     <ul>
 *         <li>对应的问卷子项中没有对应排序id的选项 </li>
 *         <del><li>数据库中{@code ItemSelect}主键 未找到</li></del>
 *     </ul>
 *     抛出
 * </p>
 * 错误码：{@code 403}
 */
public class ItemSelectNotFoundException extends BaseException {
    private final static int errorCode = 403;

    public ItemSelectNotFoundException(ItemSort itemSort, int selectID) {
        super(errorCode, informationFormat(itemSort, selectID));
    }

    public ItemSelectNotFoundException(int pk) {
        super(errorCode, BaseException.dataNotFoundInDatabase(SubItemSelect.class, pk));
    }

    public ItemSelectNotFoundException(int questionID, int itemID, int selectID) {
        super(errorCode, informationFormat(questionID, itemID, selectID));
    }

    static String informationFormat(ItemSort itemSort, int selectID) {
        return
                String.format(
                        "Item<ID:%s| Name:`%s` | Type:`%s`> Select<ID:%s> Not Found",
                        itemSort.sortIndex, itemSort.item.data, itemSort.item.type,
                        selectID
                );
    }

    static String informationFormat(int questionID, int itemID, int selectID) {
        return
                String.format(
                        "Item<ID:%s> Select<ID:%s> Not Found In Question<ID:%s>",
                        itemID,
                        selectID, questionID
                );
    }
}
