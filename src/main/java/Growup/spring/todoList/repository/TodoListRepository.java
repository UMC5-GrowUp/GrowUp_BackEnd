package Growup.spring.todoList.repository;

import Growup.spring.todoList.model.TodoList;
import Growup.spring.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TodoListRepository extends JpaRepository<TodoList, Long> {

    List<TodoList> findByUser(User user);
}
