package com.poseidon.dolphin.manager.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poseidon.dolphin.manager.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
