package Growup.spring.todoList.controller;


import Growup.spring.constant.ApiResponse;
import Growup.spring.constant.status.SuccessStatus;
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

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/growup/todo")
public class TodoListController {

    private final TodoListService todoListService;
    private final JwtProvider jwtProvider;
    /**
     * 24.01.29 작성자 : 정주현
     * 투두리스트 작성
     */
    @ResponseBody
    @PostMapping("/enroll")
    public ApiResponse<TodoDtoListRes.todoEnrollRes> todoListEnroll(@RequestBody @Valid TodoDtoListReq.todoEnrollReq request){
        Long userId = jwtProvider.getUserID();
        TodoList todoList = todoListService.todoListEnroll(request,userId);
        return ApiResponse.onSuccess(TodoListConverter.todoListDtoRes(todoList));
    }
    /**
     * 24.01.29 작성자 : 정주현
     * 투두리스트 조회
     */
    @GetMapping("/inquiry")
    public ApiResponse<TodoDtoListRes.todoResultInquiryRes> todoListInquiry(){
        Long userId = jwtProvider.getUserID();
        return ApiResponse.onSuccess(TodoListConverter.todoResultInquiryRes(userId,todoListService.todoListInquiry(userId)));
    }
    /**
     * 24.01.29 작성자 : 정주현
     * 투두리스트 내용 수정
     */
    @PatchMapping("/modify-comment")
    public ApiResponse<SuccessStatus> todoListCommentModify(@RequestParam Long todoListId,@RequestBody TodoDtoListReq.todoCommentModifyReq request){
        todoListService.todoListCommentModify(todoListId,request);
        return ApiResponse.onSuccessWithoutResult(SuccessStatus._OK);
    }
    /**
     * 24.01.29 작성자 : 정주현
     * 투두리스트 체크(상태) 수정
     */
    @PatchMapping("/modify-status")
    public ApiResponse<SuccessStatus> modifyStatus(@RequestParam Long todoListId){
        todoListService.modifyservice(todoListId);
        return ApiResponse.onSuccessWithoutResult(SuccessStatus._OK);
    }



}
