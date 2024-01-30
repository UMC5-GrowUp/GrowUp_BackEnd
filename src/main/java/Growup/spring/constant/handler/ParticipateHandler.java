package Growup.spring.constant.handler;

import Growup.spring.constant.code.BaseErrorCode;
import Growup.spring.constant.exeption.GeneralException;

public class ParticipateHandler extends GeneralException {
    public ParticipateHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
