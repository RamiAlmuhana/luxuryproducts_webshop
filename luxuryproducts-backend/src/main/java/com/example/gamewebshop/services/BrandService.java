package com.example.gamewebshop.services;

import com.example.gamewebshop.Repositorys.BrandRepository;
import com.example.gamewebshop.dao.ProductDAO;
import com.example.gamewebshop.models.Product.Brand;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class BrandService {

    private final BrandRepository brandRepository;
    private final ProductDAO productDAO;

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    public Brand getBrandByName(String name) {
        Optional<Brand> brand = brandRepository.findByBrandName(name);

        if (brand.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No brand found with that name"
            );
        }
        return brand.get();
    }

}
