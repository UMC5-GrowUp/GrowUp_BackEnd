package Growup.spring.constant.handler;

import Growup.spring.constant.code.BaseErrorCode;
import Growup.spring.constant.exeption.GeneralException;

public class UserHandler extends GeneralException {

    public UserHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}