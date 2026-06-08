package com.phuc.jobhunter.controller;

import com.phuc.jobhunter.domain.User;
import com.phuc.jobhunter.domain.dto.ResultPaginationDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.phuc.jobhunter.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;



    public UserController(UserService userService, PasswordEncoder passwordEncoder){

        this.userService=userService;
        this.passwordEncoder=passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){

        String pw = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(pw);
        User createUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        User user= userService.getUserById(id);

        return ResponseEntity.status(HttpStatus.OK).body(user);

    }
    @GetMapping("/allusers")
    public ResponseEntity<ResultPaginationDTO> getAllUser(
            @RequestParam("current") Optional<String> currentOptional,
            @RequestParam("pageSize") Optional<String> pageSizeOptional
            ){

        String sCurrent = currentOptional.isPresent() ? currentOptional.get() : "";
        String sPageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get() : "";

        Pageable pageable = PageRequest.of(Integer.parseInt(sCurrent) -1, Integer.parseInt(sPageSize));
        ResultPaginationDTO listUser = userService.getAllUser(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(listUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable Long id){
        User upUser =  userService.updateUser(id, user);
        return ResponseEntity.ok(upUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser (@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



}
