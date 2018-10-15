package com.cloud.project.service;

import com.cloud.project.model.Gundam;
import com.cloud.project.repository.GundamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GundamService {
    private final GundamRepository gundamRepository;

    public GundamService(GundamRepository gundamRepository) {
        this.gundamRepository = gundamRepository;
    }

    public List<Gundam> findAll() {
        return gundamRepository.findAll();
    }

    public Gundam findById(Integer id) {
        return gundamRepository.findById(id).get();
    }

    public void save(Gundam gundam) {
        gundamRepository.save(gundam);
    }

    public void delete(Integer id) {
        gundamRepository.delete(findById(id));
    }

}
