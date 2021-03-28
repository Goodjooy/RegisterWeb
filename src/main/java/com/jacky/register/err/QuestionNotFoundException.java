package com.jacky.register.err;

import com.jacky.register.models.respond.question.control.Question;

public class QuestionNotFoundException extends RuntimeException {
    public QuestionNotFoundException(Integer questionID){
        super("question not found :"+questionID);
    }
}
