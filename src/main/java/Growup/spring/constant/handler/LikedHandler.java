package Growup.spring.constant.handler;

import Growup.spring.constant.code.BaseErrorCode;
import Growup.spring.constant.exeption.GeneralException;

public class LikedHandler extends GeneralException {
    public LikedHandler(BaseErrorCode errorCode){
        super(errorCode);
    }
}
