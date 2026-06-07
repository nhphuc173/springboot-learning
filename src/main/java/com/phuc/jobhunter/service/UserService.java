package com.phuc.jobhunter.service;

import com.phuc.jobhunter.domain.User;
import com.phuc.jobhunter.util.error.IdInvalidException;
import org.springframework.stereotype.Service;
import com.phuc.jobhunter.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository= userRepository;
    }

    public User getUserById(Long id){
        if (id == null || id <= 0) {
            throw new IdInvalidException("ID phải lớn hơn 0");
        }

        return userRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy user với ID: " + id));
    }

    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    public User createUser(User newUser){
        return userRepository.save(newUser);
    }

    public User updateUser(Long id, User user){
        User upuser= userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("khong tim thay user"));
        upuser.setName(user.getName());
        upuser.setEmail(user.getEmail());
        upuser.setPassword(user.getPassword());

        return userRepository.save(upuser);

    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public User getUserByName(String username){
        return userRepository.findByName(username);
    }



}
