package com.cloud.project.repository;

import com.cloud.project.model.Gundam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GundamRepository extends JpaRepository<Gundam, Integer> {
}
