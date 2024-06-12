package Growup.spring.todoList.dto;

import Growup.spring.todoList.model.Enum.TodoStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class TodoDtoListRes {

    //todolist 등록 응답
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class todoEnrollRes{
        private Long userId;
        private LocalDateTime createAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class todoResultInquiryRes{
        private Long userId;
        private List<todoInquiryRes> todoListList;
    }


    //today todolist 조회
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class todoInquiryRes{
        private Long todoListId;
        private String comment;
        private TodoStatus status;
    }


}
