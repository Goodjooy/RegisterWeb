package com.jacky.register.err.register.notFound;

import com.jacky.register.err.BaseException;
import com.jacky.register.models.database.group.GroupDepartment;

public class ExamCycleNotFoundException extends BaseException {
    private static final int errorCode=404;
    public ExamCycleNotFoundException(Long id, GroupDepartment department){
        super(errorCode,
                String.format(
                        "ExamCycle<ID:%s> Not Found In Department<%s>"
                        ,id,department.name
                ));
    }
}
