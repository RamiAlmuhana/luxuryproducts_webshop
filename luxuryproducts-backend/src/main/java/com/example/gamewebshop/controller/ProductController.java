package com.example.gamewebshop.controller;

import com.example.gamewebshop.dao.ProductDAO;
import com.example.gamewebshop.dto.ProductDTO;
import com.example.gamewebshop.models.PromoCode;
import com.example.gamewebshop.dao.PromoCodeDAO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/products")
public class ProductController {

    private final ProductDAO productDAO;
    private final PromoCodeDAO promoCodeDAO;

    public ProductController(ProductDAO productDAO, PromoCodeDAO promoCodeDAO) {
        this.productDAO = productDAO;
        this.promoCodeDAO = promoCodeDAO;
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(this.productDAO.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO productDTO = this.productDAO.getProductById(id);
        if (productDTO != null) {
            Optional<PromoCode> promoCodeOptional = promoCodeDAO.getPromoCodeByCategory(productDTO.getCategoryId());
            promoCodeOptional.ifPresent(promoCode -> {
                productDTO.setPromoCode(promoCode.getCode());
                productDTO.setPromoDiscount(promoCode.getDiscount());
                productDTO.setPromoType(promoCode.getType().toString());
            });
        }
        return ResponseEntity.ok(productDTO);
    }



    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO){
        this.productDAO.updateProduct(productDTO, id);

        return ResponseEntity.ok("Updated product with id" + id);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id){
        this.productDAO.deleteById(id);

        return ResponseEntity.ok("Product deleted with id " + id);
    }

    @GetMapping(params = "categoryId")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@RequestParam Long categoryId) {
        return ResponseEntity.ok(this.productDAO.getAllProductsByCategory(categoryId));
    }
}
