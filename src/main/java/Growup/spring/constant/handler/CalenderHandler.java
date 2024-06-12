package Growup.spring.constant.handler;

import Growup.spring.constant.code.BaseErrorCode;
import Growup.spring.constant.exeption.GeneralException;

public class CalenderHandler extends GeneralException {

    public CalenderHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}

