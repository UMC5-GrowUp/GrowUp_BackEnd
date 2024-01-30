package Growup.spring.todoList.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

public class TodoDtoListReq {

    //todolist 등록 요청
    @Getter
    public static class todoEnrollReq{
        @NotBlank
        private String comment;
    }


}

