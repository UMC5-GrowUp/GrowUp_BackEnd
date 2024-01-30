package Growup.spring.constant.handler;

import Growup.spring.constant.code.BaseErrorCode;
import Growup.spring.constant.exeption.GeneralException;

public class GrowRoomHandler extends GeneralException {
    public GrowRoomHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
