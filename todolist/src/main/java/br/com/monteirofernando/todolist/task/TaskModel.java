package br.com.monteirofernando.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import br.com.monteirofernando.todolist.user.UserModel;
import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;

@Entity(name="tb_tasks")
@Data
public class TaskModel {
    
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID ID;
    private UUID userID;
    
    @Getter
    @Column(length = 50)
    private String title;
    private String description;
    private String priority;
    private LocalDateTime start_at;
    private LocalDateTime end_at;

    @CreationTimestamp
    private LocalDateTime created_at;




    public void setTitle(String title) throws Exception {
        if(title.length() > 50) {
            throw new Exception("Title should contain at most 50 characters");
        }
        this.title = title;

    }
}
