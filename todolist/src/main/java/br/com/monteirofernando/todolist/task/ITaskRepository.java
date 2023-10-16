package br.com.monteirofernando.todolist.task;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ITaskRepository extends JpaRepository<TaskModel, UUID> {
    
    public List<TaskModel> findByUserID(UUID userID);

}
