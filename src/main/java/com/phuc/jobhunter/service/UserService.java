package com.phuc.jobhunter.service;

import com.phuc.jobhunter.domain.User;
import com.phuc.jobhunter.domain.dto.*;
import com.phuc.jobhunter.util.error.IdInvalidException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.phuc.jobhunter.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;


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

    public ResultPaginationDTO getAllUser(Specification<User> specification, Pageable pageable){
        Page<User> pageUser= this.userRepository.findAll(specification,  pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();

        Meta mt = new Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());
//
        rs.setMeta(mt);


        List<ResUserDTO> listUser = pageUser.getContent()
                .stream()
                .map(item -> new ResUserDTO(
                        item.getId(),
                        item.getEmail(),
                        item.getName(),
                        item.getGender(),
                        item.getAddress(),
                        item.getAge(),
                        item.getUpdatedAt(),
                        item.getCreatedAt()
                ))
                .collect(Collectors.toList());

        rs.setResult(listUser);

        return rs;
    }

    public User createUser(User newUser){
        return userRepository.save(newUser);
    }

    public User updateUser(Long id, User user){
        User upuser= userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("khong tim thay user"));
        upuser.setName(user.getName());
        upuser.setAge(user.getAge());
        upuser.setGender(user.getGender());
        upuser.setAddress(user.getAddress());

        return this.userRepository.save(upuser);

    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public User getUserByName(String username){
        return userRepository.findByName(username);
    }


    public boolean isExitsName(String name){
        return this.userRepository.existsByName(name);
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user){
        ResCreateUserDTO res = new ResCreateUserDTO();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setCreatedAt(user.getCreatedAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());

        return res;
    }

    public ResUserDTO convertToResUserDTO(User user) {
        ResUserDTO res = new ResUserDTO();

        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setCreatedAt(user.getCreatedAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());

        return res;
    }

    public ResUpdateUserDTO convertToResUpdateUserDTO(User user) {
        ResUpdateUserDTO res = new ResUpdateUserDTO();

        res.setId(user.getId());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());

        return res;
    }


    public void updateUserToken(String token, String name){
        User currentUser= this.getUserByName(name);
        if(currentUser != null){
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }

    }


}
