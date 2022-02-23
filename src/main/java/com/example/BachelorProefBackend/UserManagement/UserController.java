package com.example.BachelorProefBackend.UserManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="userManagement/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public void addNewUser(@RequestBody User_entity user) {
        userService.addNewUser(user);
    }

    @GetMapping
    public List<User_entity> getUsers() {
        return userService.getUsers();
    }
    //Hier: voeg alle gets toe, voor alle verschillende users types
}
