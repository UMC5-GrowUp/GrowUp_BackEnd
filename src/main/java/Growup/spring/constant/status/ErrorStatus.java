package Growup.spring.constant.status;

import Growup.spring.constant.code.BaseErrorCode;
import Growup.spring.constant.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),


    // User Error(4001~

    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER4001", "사용자가 없습니다."),
    USER_ID_PASSWORD_FOUND(HttpStatus.BAD_REQUEST, "USER4002", "아이디 또는 비밀번호가 잘못되었습니다."),
    USER_NICKNAME_ERROR(HttpStatus.BAD_REQUEST, "USER4003", "이미 사용중인 닉네임 입니다."),
    USER_EMAIL_ERROR(HttpStatus.BAD_REQUEST, "USER4004" , "이메일 형식이 아닙니다."),
    USER_EMAIL_DUPLICATE(HttpStatus.BAD_REQUEST, "USER4005", "이미 사용중인 이메일 입니다."),
    USER_PASSWORD_ERROR(HttpStatus.BAD_REQUEST, "USER4006", "패스워드를 확인하세요.(최소8자,최대20자,영문자,숫자,특수문자 모두 포함되어야 합니다."),
    USER_PASSWORD_NONEQULE(HttpStatus.BAD_REQUEST, "USER4007", "비밀번호가 일치하지 않습니다."),
    USER_PASSWORD_EXIST(HttpStatus.BAD_REQUEST, "USER4008", "이전 비밀번호와 일치합니다."),
    USER_NOT_PERMITTED(HttpStatus.FORBIDDEN, "User4009", "수정/삭제 권한이 없습니다."),



    //JWT Error(4100~
    JWT_EMPTY(HttpStatus.UNAUTHORIZED,"JWT4100","JWT 토큰을 넣어주세요."),
    JWT_INVALID(HttpStatus.UNAUTHORIZED,"JWT4101","다시 로그인 해주세요."),
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED,"JWT4102","토큰이 만료되었습니다."),
    JWT_BAD(HttpStatus.UNAUTHORIZED,"JWT4102","JWT 토큰이 잘못되었습니다."),
    JWT_REFRESHTOKEN_NOT_MATCH(HttpStatus.UNAUTHORIZED,"JWT4103","RefreshToken이 일치하지 않습니다."),
    JWT_AUTHORIZATION_FAILED(HttpStatus.UNAUTHORIZED,"JWT4104","권한이 없습니다."),



    NO_SUCH_ALGORITHM(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR5001", "알고리즘 사용 불가능합니다."),
    EMAIL_VERIFY_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR5002", "인증이 만료됐습니다."),
    EMAIL_SEND_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR5003", "메일 전송이 실패되었습니다."),
    FILE_CHANGE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR5004", "파일 전환이 실패되었습니다."),


    //liked error -> 4010~
    LIKED_NOT_FOUND(HttpStatus.NOT_FOUND,"LIKED4011","liked에 해당 아이디가 없습니다."),


    //growroom error -> 4020~
    GROWROOM_NOT_FOUND(HttpStatus.NOT_FOUND,"GROWROOM4021","GrowRoom이 없습니다."),
    GROWROOM_NOT_HOT(HttpStatus.BAD_REQUEST,"GROWRROM4022","인기글이 아닙니다."),
    GROWROOM_IS_DELETED(HttpStatus.BAD_REQUEST, "GROWROOM4023", "삭제된 GrowRoom입니다."),


    // recruitment error
    RECRUITMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "RECRUITMENT4001", "Recruitment가 없습니다."),

    // number error
    NUMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "NUMBER4001", "number가 없습니다."),

    // period error
    PERIOD_NOT_FOUND(HttpStatus.NOT_FOUND, "PERIOD4001", "period가 없습니다."),

    // pin error
    PIN_NOT_FOUND(HttpStatus.NOT_FOUND, "PIN4001", "해당 댓글이 없습니다."),

    //participate error error -> 4030~
    PARTICIPATE_NOT_FOUND(HttpStatus.BAD_REQUEST,"PARTICIPATE4031","Participate가 없습니다."),
    PARTICIPATE_NO_AUTHORIZATION(HttpStatus.BAD_REQUEST, "PARTICIPATE4032","방장만 좋아요가 가능 합니다."),

    //todolist 관련 에러코드(4041~
    TODOLIST_NOT_FOUND(HttpStatus.BAD_REQUEST, "TODOLIST4041", "등록되지 않은 TODOLIST 입니다."),


    //calender 관련 에러코드(4051~
    CALENDER_NOT_FOUND(HttpStatus.BAD_REQUEST, "CALENDER4051", "등록되지 않은 CALENDER 글 입니다.")
    ;


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }

}
