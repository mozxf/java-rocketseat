package br.com.monteirofernando.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Result;
import br.com.monteirofernando.todolist.user.IUserRepository;
import br.com.monteirofernando.todolist.user.UserModel;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                var servletPath = request.getServletPath();
                if(servletPath.startsWith("/tasks/")  ) {
                     var token = request.getHeader("Authorization").split(" ")[1];
              String[] credentials = this.getUserCredentials(token);
              String username = credentials[0];
              String password = credentials[1];

              UserModel user = this.userRepository.findByUsername(username);

              if(user == null) {
                response.sendError(401);
            }
            else {
                Result result = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword().toCharArray());
                if(result.verified) {
                    request.setAttribute("userID", user.getID());
                    filterChain.doFilter(request, response);
                    
                }
                else {
                    response.sendError(401);
                    
              }

            }

                }
                else {
                    filterChain.doFilter(request, response);
                }
              


    }

    private String[] getUserCredentials(String token) {
          byte[] decodedAuth = Base64.getDecoder().decode((token));
                String auth = new String(decodedAuth);
                String[] credentials = auth.split(":");
                
        return credentials;
    }


   
    
}
