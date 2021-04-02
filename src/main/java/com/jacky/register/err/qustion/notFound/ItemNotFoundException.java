package com.jacky.register.err.qustion.notFound;

import com.jacky.register.err.BaseException;
import com.jacky.register.models.database.quetionail.Questionable;
import com.jacky.register.models.database.quetionail.subItems.QuestionSubItem;

/**
 * {@code ItemNotFoundException}
 * <p>
 * 在
 *     <ul>
 *         <li>对应的问卷下找不到指定排序ID索引 </li>
 *         <li>数据库中{@code Item}主键 未找到</li>
 *     </ul>
 *     抛出
 * </p>
 * 错误码：{@code 402}
 */
public class ItemNotFoundException extends BaseException {

    public ItemNotFoundException(Questionable questionable, int ItemID) {
        super(402, informationFormat(questionable, ItemID));
    }

    public ItemNotFoundException(int ItemPK) {
        super(402, informationFormat(ItemPK));
    }

    public ItemNotFoundException(int questionID, int itemID) {
        super(402,informationFormat(questionID, itemID));
    }

     static String informationFormat(Questionable question, int id) {
        return
                String.format(
                        "Item<ID:%s> Not Found In Question<ID:%s | NAME :`%s`>",
                        id, question.ID, question.title
                );
    }

     static String informationFormat(int pk) {
        return
                BaseException.dataNotFoundInDatabase(QuestionSubItem.class, pk);
    }
    static String informationFormat(int question, int id) {
        return
                String.format(
                        "Item<ID:%s> Not Found In Question<ID:%s>",
                        id, question
                );
    }
}
