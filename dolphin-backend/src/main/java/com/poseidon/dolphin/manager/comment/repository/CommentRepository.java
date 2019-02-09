package com.poseidon.dolphin.manager.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poseidon.dolphin.manager.comment.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
