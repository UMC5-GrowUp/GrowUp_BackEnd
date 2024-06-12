package Growup.spring.constant.handler;

import Growup.spring.constant.code.BaseErrorCode;
import Growup.spring.constant.exeption.GeneralException;

public class TodoListHandler extends GeneralException {
    public TodoListHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
