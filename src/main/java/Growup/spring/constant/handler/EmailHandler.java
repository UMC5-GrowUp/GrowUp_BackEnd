package Growup.spring.constant.handler;

import Growup.spring.constant.code.BaseErrorCode;
import Growup.spring.constant.exeption.GeneralException;

public class EmailHandler extends GeneralException {

    public EmailHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
