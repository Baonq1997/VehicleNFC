package com.cloud.project.controller;

import com.cloud.project.model.Gundam;
import com.cloud.project.service.GundamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping(value = "/gundam")
public class GundamController {
    private final GundamService gundamService;

    public GundamController(GundamService gundamService) {
        this.gundamService = gundamService;
    }

    @GetMapping("/index")
    public ModelAndView index(ModelAndView mav) {
        mav.setViewName("index");
        return mav;
    }

    @GetMapping("/management")
    public ModelAndView productPage(ModelAndView mav) {
        mav.setViewName("product");
        return mav;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Gundam> getById(@PathVariable("id") Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(gundamService.findById(id));
    }

    @GetMapping("/get-gundams")
    public ResponseEntity<List<Gundam>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(gundamService.findAll());
    }

    @GetMapping("/save-product")
    public String saveProduct(Gundam gundam){
        try{
            gundamService.save(gundam);
            return "Success";
        } catch (Exception e) {
            return "Failed to save product";
        }
    }

    @PostMapping("/delete-product")
    public String deleteProduct(Integer id) {
        try{
            gundamService.delete(id);
            return "Success";
        } catch (Exception e) {
            return "Failed to delete this product";
        }
    }

    @PostMapping("/create-product")
    public String createProduct(Gundam gundam) {
        try {
            gundamService.save(gundam);
            return "Success";
        }catch (Exception e){
            return "Failed to create this product";
        }
    }
}
