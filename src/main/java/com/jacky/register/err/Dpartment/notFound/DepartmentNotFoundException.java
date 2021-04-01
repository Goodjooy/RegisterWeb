package com.jacky.register.err.Dpartment.notFound;

import com.jacky.register.err.BaseException;
import com.jacky.register.models.database.group.GroupDepartment;

/**
 * {@code DepartmentNotFoundException}
 * <p>
 * 在
 *     <ul>
 *         <li>数据库中{@code GroupDepartment}主键 未找到</li>
 *     </ul>
 *     抛出
 * </p>
 * 错误码：{@code 404}
 */
public class DepartmentNotFoundException extends BaseException {
    private final static int errorCode=404;
    public DepartmentNotFoundException(int id){
        super(errorCode,BaseException.dataNotFoundInDatabase(GroupDepartment.class,id));
    }
}
