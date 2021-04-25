package com.jacky.register.dataHandle.dataCheck;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class DataCheck {



    enum CheckType{
        CREATE,
        UPDATE
    }

    public static <IN extends Serializable,OUT> OUT checkData(Class<OUT> targetClazz,IN inputData,CheckType checkType){
        return null;
    }

    static <IN extends Serializable,OUT> OUT checkCreate(Class<OUT> targetClazz,IN inputData){
        var InClazz=inputData.getClass();

        Constructor<OUT> outDataConstruct= null;
        try {
            outDataConstruct = targetClazz.getConstructor();
            OUT outData= outDataConstruct.newInstance();

        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            // TODO: 2021/4/25 no constructor find
            e.printStackTrace();
        }
return null;

    }
    static <IN extends Serializable,OUT> OUT checkUpdate(Class<OUT> targetClazz,IN inputData){
        return null;
    }
    static <IN extends Serializable> void checkInput(IN inputData){
    }
}
