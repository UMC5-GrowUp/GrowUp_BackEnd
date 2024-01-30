package Growup.spring.todoList.controller;


import Growup.spring.constant.ApiResponse;
import Growup.spring.security.JwtProvider;
import Growup.spring.todoList.converter.TodoListConverter;
import Growup.spring.todoList.dto.TodoDtoListReq;
import Growup.spring.todoList.dto.TodoDtoListRes;
import Growup.spring.todoList.model.TodoList;
import Growup.spring.todoList.service.TodoListImpl;
import Growup.spring.todoList.service.TodoListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/growup/todo")
public class TodoListController {

    private final TodoListService todoListService;
    private final JwtProvider jwtProvider;

    @ResponseBody
    @PostMapping("/enroll")
    public ApiResponse<TodoDtoListRes.todoEnrollRes> todoListEnroll(@RequestBody TodoDtoListReq.todoEnrollReq request){
        Long userId = jwtProvider.getUserID();
        TodoList todoList = todoListService.todoListEnroll(request,userId);
        return ApiResponse.onSuccess(TodoListConverter.todoListDtoRes(todoList));
    }

    @GetMapping("/search-today")
    public ApiResponse<TodoDtoListRes.todoResultSearchRes> todoListSearch(){
        Long userId = jwtProvider.getUserID();
        return ApiResponse.onSuccess(TodoListConverter.todoResultSearchRes(userId,todoListService.todoListSearch(userId)));
    }





}
