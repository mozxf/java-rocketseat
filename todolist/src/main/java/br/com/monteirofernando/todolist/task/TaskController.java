package br.com.monteirofernando.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.monteirofernando.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    
    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel task, HttpServletRequest request) {
    var userID = request.getAttribute("userID");
    task.setUserID((UUID)userID);

    var currentDate = LocalDateTime.now();

    if(currentDate.isAfter(task.getStart_at()) || currentDate.isAfter(task.getEnd_at())) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Start/End date should be greater than current date");
    }
    if(task.getStart_at().isAfter(task.getEnd_at())) {
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Start date should be lower than End date");
    }

        TaskModel createdTask = this.taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.OK).body(createdTask);
    }

    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request)  {

        var userID = request.getAttribute("userID");
        List<TaskModel> tasks = this.taskRepository.findByUserID((UUID)userID);

        return tasks;
    }


    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable UUID id, HttpServletRequest request, @RequestBody TaskModel task) {
        UUID userID = (UUID)request.getAttribute("userID");
        TaskModel repoTask = this.taskRepository.getReferenceById(id);
        
       
        if(repoTask != null) {
             if(!repoTask.getUserID().equals(userID)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task not found");
        }

            Utils.copyNonNullProperties(task, repoTask);
            return ResponseEntity.status(HttpStatus.OK).body(this.taskRepository.save(repoTask));
        }

       return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task not found");
    }


}
