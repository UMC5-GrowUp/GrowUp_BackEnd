package Growup.spring.todoList.converter;

import Growup.spring.todoList.dto.TodoDtoListReq;
import Growup.spring.todoList.dto.TodoDtoListRes;
import Growup.spring.todoList.model.TodoList;
import Growup.spring.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TodoListConverter {

    //todolist 객체 생성
    public static TodoList todoList(User user, TodoDtoListReq.todoEnrollReq request){
        return TodoList.builder()
                .user(user)
                .comment(request.getComment())
                .build();
    }

    //todolist 등록 응답
    public static TodoDtoListRes.todoEnrollRes todoListDtoRes(TodoList todoList){
        return TodoDtoListRes.todoEnrollRes .builder()
                .userId(todoList.getUser().getId())
                .createAt(todoList.getCreatedAt())
                .build();
    }

    //todolist 조회 리스트
    public static TodoDtoListRes.todoSearchRes todoSearchRes(TodoList todoList) {
        return TodoDtoListRes.todoSearchRes.builder()
                .todoListId(todoList.getId())
                .comment(todoList.getComment())
                .status(todoList.getStatus())
                .build();
    }
    //todolist 조회 리스트+ Userid 포함
    public static TodoDtoListRes.todoResultSearchRes todoResultSearchRes(Long userId,List<TodoDtoListRes.todoSearchRes> todoList) {
        return TodoDtoListRes.todoResultSearchRes.builder()
                .userId(userId)
                .todoListList(todoList)
                .build();
    }
}
