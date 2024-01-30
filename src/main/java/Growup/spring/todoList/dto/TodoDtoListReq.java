package Growup.spring.todoList.dto;

import lombok.Getter;

public class TodoDtoListReq {

    //todolist 등록 요청
    @Getter
    public static class todoEnrollReq{
        private String comment;
    }


}

