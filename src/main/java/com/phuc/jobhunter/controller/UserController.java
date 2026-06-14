package com.phuc.jobhunter.controller;

import com.phuc.jobhunter.domain.User;
import com.phuc.jobhunter.domain.dto.ResCreateUserDTO;
import com.phuc.jobhunter.domain.dto.ResUpdateUserDTO;
import com.phuc.jobhunter.domain.dto.ResUserDTO;
import com.phuc.jobhunter.domain.dto.ResultPaginationDTO;
import com.phuc.jobhunter.util.annotation.ApiMessage;
import com.phuc.jobhunter.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    @ApiMessage("create a new user")
    public ResponseEntity<ResCreateUserDTO> createUser(@Valid @RequestBody User user){

        boolean isExitsName = this.userService.isExitsName(user.getName());

        if(isExitsName) throw  new IdInvalidException("ten" +user.getName()+ " da ton tai");
        String pw = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(pw);
        User createUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResCreateUserDTO(createUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable Long id){
        User user= userService.getUserById(id);

        return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertToResUserDTO(user));

    }
    @GetMapping("/allusers")
    @ApiMessage("get all user")
    public ResponseEntity<ResultPaginationDTO> getAllUser(
            @Filter Specification<User> spec,
            Pageable pageable

            ){
//
//        String sCurrent = currentOptional.isPresent() ? currentOptional.get() : "";
//        String sPageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get() : "";
//
//        Pageable pageable = PageRequest.of(Integer.parseInt(sCurrent) -1, Integer.parseInt(sPageSize));
//        ResultPaginationDTO listUser = userService.getAllUser(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.getAllUser(spec,pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User user, @PathVariable Long id){
        User upUser =  userService.updateUser(id, user);
        return ResponseEntity.ok(this.userService.convertToResUpdateUserDTO(upUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser (@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



}
