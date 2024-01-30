package Growup.spring.todoList.service;

import Growup.spring.constant.handler.UserHandler;
import Growup.spring.constant.status.ErrorStatus;
import Growup.spring.todoList.converter.TodoListConverter;
import Growup.spring.todoList.dto.TodoDtoListReq;
import Growup.spring.todoList.dto.TodoDtoListRes;
import Growup.spring.todoList.model.TodoList;
import Growup.spring.todoList.repository.TodoListRepository;
import Growup.spring.user.model.User;
import Growup.spring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoListImpl implements TodoListService {

    private final TodoListRepository todoListRepository;
    private final UserRepository userRepository;

    //todolist 등록
    @Override
    public TodoList todoListEnroll(TodoDtoListReq.todoEnrollReq request,Long userId){
        User user = userRepository.findById(userId).orElseThrow(()-> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        return todoListRepository.save(TodoListConverter.todoList(user,request));
    }

    //todolist 조회 - 오늘 날짜만 조회
    @Override
    public List<TodoDtoListRes.todoSearchRes> todoListSearch(Long userId){

        User user = userRepository.findById(userId).orElseThrow(()->new UserHandler(ErrorStatus.USER_NOT_FOUND));

        LocalDate today = LocalDate.now();

        return todoListRepository.findByUser(user)
                .stream()
                .filter(todoList -> todoList.getCreatedAt().toLocalDate().isEqual(today))  // Filtering 오늘날짜만 조회
                .map(TodoListConverter::todoSearchRes)
                .collect(Collectors.toList());

    }

}
