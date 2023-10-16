package br.com.monteirofernando.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
private IUserRepository userRepository;
@PostMapping("/")
    public ResponseEntity Create (@RequestBody UserModel user) {

        UserModel userExists = this.userRepository.findByUsername(user.getUsername());
        
        if(userExists != null) {
            System.out.println("Usuário já existe");
            return ResponseEntity.badRequest().body("Usuário já existe") ;
        }
        
        String encryptedPassword =  BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());

        user.setPassword(encryptedPassword);
        UserModel userCreated = this.userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }

    
    
}
