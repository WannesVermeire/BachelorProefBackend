package com.example.BachelorProefBackend.UserManagement.User;

import com.example.BachelorProefBackend.SubjectManagement.Subject.Subject;
import com.example.BachelorProefBackend.SubjectManagement.Subject.SubjectService;
import com.example.BachelorProefBackend.UserManagement.FileStorage.ResponseMessage;
import com.example.BachelorProefBackend.UserManagement.FileStorage.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.*;


@Slf4j
@RestController
@RequestMapping(path="userManagement/users")
public class UserController {
    private final UserService userService;
    private final SubjectService subjectService;
    private final StorageService storageService;

    @Autowired //instantie van userService automatisch aangemaakt en in deze constructor gestoken (Dependency injection)
    public UserController(UserService userService, SubjectService subjectService, StorageService storageService) {
        this.userService = userService;
        this.subjectService = subjectService;
        this.storageService = storageService;
    }

    @GetMapping
    public List<User_entity> getAllUsers() {return userService.getAllUsers();}
    @GetMapping(path="{userId}")
    public User_entity getUserById(@PathVariable("userId") long id) {return userService.getUserById(id);}
    @GetMapping(path="student")
    public List<User_entity> getAllStudents() {
        return userService.getAllStudents();
    }
    @GetMapping(path="administrator")
    public List<User_entity> getAllAdministrators() {
        return userService.getAllAdministrators();
    }
    @GetMapping(path="promotor")
    public List<User_entity> getAllPromotors() {
        return userService.getAllPromotors();
    }
    @GetMapping(path="coordinator")
    public List<User_entity> getAllCoordinators() {
        return userService.getAllCoordinators();
    }
    @GetMapping(path="student/{userId}/preferredSubjects")
    public List<Subject> getPreferredSubjects(@PathVariable("userId") long id) {return userService.getPreferredSubjects(id);}
//    //Mapping based on URL query example
//    @GetMapping
//    @ResponseBody
//    public List<User_entity> getUsers(@RequestParam(defaultValue = "null") String id, @RequestParam(defaultValue = "null") String type) {
//        return userService.getUsers(id,type);
//    }


    @PostMapping
    public void addNewUser(@RequestParam String firstName, String lastName, String email, String telNr, String password) {
        userService.addNewUser(new User_entity(firstName, lastName, email, telNr, password));
    }

    @PostMapping(path="batch")
    public ResponseEntity<ResponseMessage> addNewUserBatch(@RequestParam("file") MultipartFile file){
        String message = "";
        try {
            storageService.save(file);
            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            userService.addNewUserBatch(); // Creating users from the file
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));

        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!" + e;
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }


    //DELETE
    @DeleteMapping(path="{userId}")
    public void deleteUser(@PathVariable("userId") long id) {
        userService.deleteUser(id);
    }

    //PUT
    @PutMapping(path="{userId}")
    public void updateUser(@PathVariable("userId") long id,
                           @RequestParam(required = false) String firstName,
                           @RequestParam(required = false) String lastName,
                           @RequestParam(required = false) String email,
                           @RequestParam(required = false) String telNr,
                           @RequestParam(required = false) String password) {
        userService.updateUser(id, firstName, lastName, email, telNr, password);
    }


    @PostMapping(path="student/addPreferredSubject")
    public void addNewPreferredSubject(@RequestParam long userId, long subjectId){
        Subject subject = subjectService.getSubjectById(subjectId);
        userService.addNewPreferredSubject(userId, subject);
    }

    //AUTHENTICATION
    @PostMapping(path="addRoleToUser")
    public ResponseEntity<?> addRoleToUser(@RequestParam String email, String roleName){
        userService.addRoleToUser(email, roleName);
        return ResponseEntity.ok().build();
    }

//    public String currentUserName(Principal principal) {
//        return principal.getName();
//    }



}