package com.jacky.register.err.qustion.notFound;

import com.jacky.register.err.BaseException;


/**
 * {@code QuestionNotFoundException}
 * <p>
 * 在试图查找
 * <ul>
 *  <li>`不存在的问卷调查【管理员】`</li>
 *  <li>`未公开的问卷调查【普通用户】`</li>
 * </ul>
 * 抛出
 * </p>
 * <p>
 * 异常码: {@code 401}
 */
public class QuestionNotFoundException extends BaseException {
    public QuestionNotFoundException(Integer questionID) {
        super(401, informationFormat(questionID));
    }

    static String informationFormat(int questionID) {
        return String.format("Question<ID:%s> Not Found In Database `Questionable`", questionID);
    }

}
