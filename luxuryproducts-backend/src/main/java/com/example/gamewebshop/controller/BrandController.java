package com.example.gamewebshop.controller;


import com.example.gamewebshop.models.Product.Brand;
import com.example.gamewebshop.services.BrandService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201"})
@RequestMapping("/brand")
public class BrandController {

    private final BrandService brandService;

    @GetMapping
    public ResponseEntity<List<Brand>> getAllBrands(){
        return ResponseEntity.ok(this.brandService.getAllBrands());
    }


    @GetMapping("/{name}")
    public ResponseEntity<Brand> getBrandbyName(@PathVariable String name){
        return ResponseEntity.ok(this.brandService.getBrandByName(name));
    }

}
