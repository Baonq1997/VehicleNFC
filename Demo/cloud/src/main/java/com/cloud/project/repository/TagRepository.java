package com.cloud.project.repository;

import com.cloud.project.model.Tag;
import com.cloud.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Integer> {

}
