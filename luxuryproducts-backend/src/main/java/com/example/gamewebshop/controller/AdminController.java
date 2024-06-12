package com.example.gamewebshop.controller;

import com.example.gamewebshop.dto.ProductVariantDTOS.*;
import com.example.gamewebshop.models.CustomUser;
import com.example.gamewebshop.models.Product.Product;
import com.example.gamewebshop.services.AdminService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200"})
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;



    @PostMapping("/create-product")
    public ResponseEntity<String> createProduct(@RequestBody CreateProductDTO createProductDTO) {
        adminService.createProduct(createProductDTO);

        return ResponseEntity.ok("Created product");

    }
    @GetMapping("/users")
    public ResponseEntity<List<CustomUser>>getAllUsers() {


        return adminService.getAllUsers();

    }


    @PostMapping("/product-variant")
    public ResponseEntity<String> createProductVariant(@RequestBody CreateProductVariantDTO createProductVariantDTO) {
        adminService.createProductVariant(createProductVariantDTO);

        return ResponseEntity.ok("Created product variant");

    }


    @DeleteMapping("/product-variant")
    public void deleteProductVariant(@RequestBody DeleteVariantDTO deleteVariantDTO) {
        adminService.deleteProductVariant(deleteVariantDTO);
    }

    @GetMapping()
    public ResponseEntity<List<Product>>productsThatCanBeDeleted() {
        return adminService.productsThatCanBeDeleted();
    }


    @PutMapping("/product-variant")
    public ResponseEntity<String> updateProductVariant(@RequestBody UpdateProductVariatieDTO updateProductVariatieDTO) {
        adminService.updateProductVariant(updateProductVariatieDTO);
        return ResponseEntity.ok("updated product variant");

    }


    @PutMapping("/stock")
    public ResponseEntity<String> changeStock(@RequestBody AddStockDTO addStockDTO) {
        adminService.addStock(addStockDTO);
        return ResponseEntity.ok("updated stock");

    }

    @PutMapping("/productionStop")
    public ResponseEntity<String> productionStop(@RequestBody ProductionStopDTO productionStopDTO) {

        adminService.productionStop(productionStopDTO);
        return ResponseEntity.ok("Succesful ProductionStop");

    }




}
