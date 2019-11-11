package org.ajude.controllers;

import org.ajude.dtos.UserNameEmail;
import org.ajude.dtos.UserProfile;
import org.ajude.entities.User;
import org.ajude.exceptions.EmailAlreadyRegisteredException;
import org.ajude.exceptions.NotFoundException;
import org.ajude.services.JwtService;
import org.ajude.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    private JwtService jwtService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserNameEmail> createUser(@RequestBody User user)
            throws EmailAlreadyRegisteredException, MessagingException {

        return new ResponseEntity<>(this.userService.createUser(user), HttpStatus.CREATED);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserProfile> getUser(@PathVariable String username) throws NotFoundException {
        return new ResponseEntity(this.userService.getUserByUsername(username), HttpStatus.OK);
    }

    /*@DeleteMapping
    public ResponseEntity deleteComment(@RequestHeader("Authorization") String header,
                                        @RequestBody String idComment){

        try{
            String email = jwtService.getSubjectByToken(jwtService.getSubjectByHeader(header));

            if(jwtService.userHasPermission(header, email))
                userService.deleteComment(userService.getUserByEmail(email).get(), idComment);

        } catch (ServletException e) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

    }*/
}
