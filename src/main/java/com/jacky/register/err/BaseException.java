package com.jacky.register.err;

import com.jacky.register.dataHandle.Result;

/**
 * 5XX 待定
 * 4XX NOT FOUND
 * 3XX BAD TYPE
 * 2XX REQUIRE NOT SATISFY
 * 1XX Operate Failure
 * 0XX <---->
 *
 */
public abstract class  BaseException extends RuntimeException{
    private final int code;

    public BaseException(int code){
        super();
        this.code=code;
    }

    public BaseException(int code,String message){
        super(message);
        this.code=code;
    }
    public BaseException(int code ,String message,Throwable throwable){
        super(message,throwable);
        this.code=code;
    }
    public BaseException(int code,Throwable throwable){
        super(throwable);
        this.code=code;
    }
    public BaseException(int code ,String message,Throwable throwable,boolean enableSuppression,boolean writableStackTrace){
        super(message,throwable,enableSuppression,writableStackTrace);
        this.code=code;
    }

    public Result<?>toResult(){
        return Result.failureResult(this);
    }

    protected static String dataNotFoundInDatabase(String dataBaseName,int id){
        return String.format(
                "Data<ID:%s> Not Found In Database `%s`",
                dataBaseName,id
        );
    }
    protected static String dataNotFoundInDatabase(Class<?>database,int id){
        return dataNotFoundInDatabase(database.getName(),id);
    }

}
