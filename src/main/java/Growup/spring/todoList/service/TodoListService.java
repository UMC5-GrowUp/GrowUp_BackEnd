package Growup.spring.todoList.service;

import Growup.spring.todoList.dto.TodoDtoListReq;
import Growup.spring.todoList.dto.TodoDtoListRes;
import Growup.spring.todoList.model.TodoList;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TodoListService {

    TodoList todoListEnroll(TodoDtoListReq.todoEnrollReq request,Long userId);

    List<TodoDtoListRes.todoInquiryRes>  todoListInquiry(Long userId);

    void modifyservice(Long todoListId);

    void todoListCommentModify(Long todoListId,TodoDtoListReq.todoCommentModifyReq request);

}
