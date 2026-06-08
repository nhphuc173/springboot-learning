package com.phuc.jobhunter.service;

import com.phuc.jobhunter.domain.User;
import com.phuc.jobhunter.domain.dto.Meta;
import com.phuc.jobhunter.domain.dto.ResultPaginationDTO;
import com.phuc.jobhunter.util.error.IdInvalidException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.phuc.jobhunter.repository.UserRepository;



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

    public ResultPaginationDTO getAllUser(Pageable pageable){
        Page<User> pageUser= this.userRepository.findAll(pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();

        Meta mt = new Meta();
        mt.setPage(pageUser.getNumber());
        mt.setPageSize(pageUser.getSize());
        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageUser.getContent());

        return rs;
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
