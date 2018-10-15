package com.cloud.project.repository;

import com.cloud.project.model.Manufacture;
import com.cloud.project.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManufactureRepository extends JpaRepository<Manufacture, Integer> {

}
