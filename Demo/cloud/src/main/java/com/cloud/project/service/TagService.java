package com.cloud.project.service;

import com.cloud.project.model.Tag;
import com.cloud.project.model.User;
import com.cloud.project.repository.TagRepository;
import com.cloud.project.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> getAllTags(){
        return tagRepository.findAll();
    }
}
