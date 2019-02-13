package com.poseidon.dolphin.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poseidon.dolphin.comment.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
