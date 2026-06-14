package com.phuc.jobhunter.repository;

import com.phuc.jobhunter.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long>, JpaSpecificationExecutor<User> {

    public User findByName(String name);
    List<User> findAll(Specification pageable);

    boolean existsByName(String name);
}
