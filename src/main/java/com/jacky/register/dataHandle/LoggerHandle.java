package com.jacky.register.dataHandle;


import com.jacky.register.err.BaseException;
import com.sun.istack.NotNull;
import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class LoggerHandle {
    private Logger logger;

    public static LoggerHandle newLogger(String name) {
        var logger = new LoggerHandle();
        logger.logger = LoggerFactory.getLogger(name);
        return logger;
    }

    public static LoggerHandle newLogger(Class<?> className) {
        var logger = new LoggerHandle();
        logger.logger = LoggerFactory.getLogger(className);
        return logger;
    }

    public void info(@NotNull String format, Object... objects) {
        var s = String.format(format, objects);
        logger.info(s);
    }

    public void error(Throwable e, @NotNull String format, Object... objects) {
        var s = String.format(format, objects);
        logger.error(s, e);
    }
    public void error(HttpServletRequest request,Throwable e){
        var message=String.format("Request URI<URI : `%s` | METHOD : `%s` | IP : `%s`>",
                request.getRequestURI(),request.getMethod(),request.getRemoteAddr());
        error(message,e);
    }

    public void error(String message, Throwable e) {
        if (e == null)
            logger.error(message);
        if (e instanceof BaseException ||e instanceof ClientAbortException) {
            logger.error(message + "| Exception<" + e.getClass().getName() + ">: " + e.getMessage());
        } else
            logger.error(message, e);
    }

    public void dataAccept(Info<?> info) {
        logger.info(String.format("`Accept Data` [%s<%s>:%s]"
                , info.name, info.valueType, info.value
        ));
    }

    private String extraInformation(Info<?>... infoList) {
        var builder = new StringBuilder();
        for (Info<?> info : infoList) {
            builder.append(String.format(" [%s<%s>: %s]",
                    info.name, info.valueType.getName(), info.value));
        }
        return builder.toString();
    }

    public void SuccessOperate(String operate,Info<?>... info ){
        logger.info(
                String.format("`%s` Success | %s",operate,extraInformation(info))
        );
    }


    public void error(Throwable exception) {
        error(exception.getMessage(), exception);
    }
}
