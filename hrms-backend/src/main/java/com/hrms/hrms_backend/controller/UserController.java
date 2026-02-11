package com.hrms.hrms_backend.controller;

import com.hrms.hrms_backend.dto.user.UpdateUser;
import com.hrms.hrms_backend.entity.User;
import com.hrms.hrms_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping({"/{email}"})
    public ResponseEntity<User> getUserByEmail(@PathVariable String email){
        return ResponseEntity.ok(userService.getByEmail(email));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateUser> updateUser(@PathVariable Long id, @RequestBody UpdateUser userDto){
        return ResponseEntity.ok(userService.update(id,userDto));
    }
}
