package com.cloud.project.controller;

import com.cloud.project.model.Tag;
import com.cloud.project.model.User;
import com.cloud.project.service.TagService;
import com.cloud.project.service.UserService;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/tag")
public class TagController {
    //    private final GundamService gundamService;
//    private final UserService userService;
    private final TagService tagService;


    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/get-tags")
    public List<Tag> getTags() {
        return tagService.getAllTags();
    }


}
