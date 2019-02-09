package com.poseidon.dolphin.simulator.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poseidon.dolphin.simulator.comment.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
