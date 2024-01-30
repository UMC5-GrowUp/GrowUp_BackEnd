package Growup.spring.constant.handler;


import Growup.spring.constant.code.BaseErrorCode;
import Growup.spring.constant.exeption.GeneralException;

public class JwtHandler extends GeneralException {

    public JwtHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}