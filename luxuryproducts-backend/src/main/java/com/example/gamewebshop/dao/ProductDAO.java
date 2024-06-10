package com.example.gamewebshop.dao;

import com.example.gamewebshop.Repositorys.CategoryRepository;
import com.example.gamewebshop.Repositorys.ProductRepository;
import com.example.gamewebshop.Repositorys.ProductVariatieRepository;
import com.example.gamewebshop.dto.ProductVariantDTOS.ProductDTO;
import com.example.gamewebshop.models.Product.Product;
import com.example.gamewebshop.models.Product.ProductVariant;
import com.example.gamewebshop.models.Product.ProductVariatie;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
@AllArgsConstructor
@Component
public class ProductDAO {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductVariatieRepository productVariatieRepository;


    public List<Product> returnProductsWithVariants(List<Product> products){
        List<Product> productsWithVariants = new ArrayList<>();
        for (Product product: products){
            if (product.getProductVariants().isEmpty()
            ) {
                continue;
            } else {
                productsWithVariants.add(product);
            }
        }
        return productsWithVariants;
    }


    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public Product getProductById(long id){
        Optional<Product> product = this.productRepository.findById(id);

        if (product.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No products found with that id"
            );
        }
        return product.get();
    }

    public List<Product> getAllProductsByCategory(long id){
        Optional<List<Product>> products =this.productRepository.findByCategoryId(id);

        if (products.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No products found with that category id"
            );
        }
        return returnProductsWithVariants(products.get());
    }

    public void changeStockOfOrderedProducts(Product product, Long quantityBought, String imageUrl ,String size){


        Optional<Product> existingProduct = this.productRepository.findById(product.getId());

        if (existingProduct.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No products with that id"
            );
        }

        ProductVariant productVariantChosen = new ProductVariant();
        for (ProductVariant productVariant: existingProduct.get().getProductVariants()){
            if (Objects.equals(productVariant.getProductImages().getFirst().getImageUrl(), imageUrl)){
                productVariantChosen = productVariant;
            }
        }
        ProductVariatie productVariatie63rd = new ProductVariatie();
        for (ProductVariatie productVariatie1: productVariantChosen.getProductVariatie()){
            if (productVariatie1.getSize().getSize().name().equals(size)) {
                productVariatie63rd = productVariatie1;
            }
        }
        Optional<ProductVariatie> productVariatie = productVariatieRepository.findById(productVariatie63rd.getId());

        if (productVariatie.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No productsvariatie with that id"
            );
        }
        ProductVariatie existingVariatie = productVariatie.get();
        existingVariatie.setQuantity_in_stock(existingVariatie.getQuantity_in_stock() - quantityBought );
        productRepository.save(product);
        productVariatieRepository.save(existingVariatie);

    }

    public void updateProduct(ProductDTO productDTO, Long id){
        Optional<Product> product = this.productRepository.findById(id);

        if (product.isPresent()){
            product.get().setName(productDTO.name);

            this.productRepository.save(product.get());
        }
    }

    public void deleteById(Long id) {
        this.productRepository.deleteById(id);
    }

    public List<Product> getProductsByColor(String color) {
        List<Product> products = productRepository.findAll();
        List<Product> productListOfSpecifiedColor = new ArrayList<>();
        List<Product> productsWithVariants = returnProductsWithVariants(products);
        for (Product product : productsWithVariants) {
            List<ProductVariant> productVariants = product.getProductVariants();
            for (ProductVariant productVariant: productVariants) {
                if (Objects.equals(productVariant.getColor().getName().toString(), color.toUpperCase())) {
                    List<ProductVariant> productVariantList = new ArrayList<>();
                    productVariantList.add(productVariant);
                    Product product1 = new Product(product.getId(), product.getName(), product.getCountry() ,product.getBrand(), product.getQuantity(), productVariantList, product.getCategory());
                    productListOfSpecifiedColor.add(product1);
                } else {
                    continue;
                }
            }
        }

        return productListOfSpecifiedColor;
    }

    public List<Product> getProductsBySizeAndFit(String fit) {

        List<Product> products = productRepository.findAll();
        List<Product> productListOfSpecifiedSizeAndFit = new ArrayList<>();
        List<Product> productsWithVariants = returnProductsWithVariants(products);

        for (Product product : productsWithVariants) {
            List<ProductVariant> productVariants = product.getProductVariants();
            for (ProductVariant productVariant: productVariants) {
                if (Objects.equals(productVariant.getSize_and_fit().getFit().toString(), fit)) {
                    List<ProductVariant> productVariantList = new ArrayList<>();
                    productVariantList.add(productVariant);
                    Product product1 = new Product(product.getId(), product.getName(), product.getCountry() ,product.getBrand(), product.getQuantity(), productVariantList, product.getCategory());
                    productListOfSpecifiedSizeAndFit.add(product1);
                } else {
                    continue;
                }
            }
        }
        return productListOfSpecifiedSizeAndFit;
    }

    public List<Product> getProductsWithVariants() {
        List<Product> products = this.productRepository.findAll();
        return returnProductsWithVariants(products);
    }
}
