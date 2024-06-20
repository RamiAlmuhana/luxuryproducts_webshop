package com.example.gamewebshop.services;

import com.example.gamewebshop.Repositorys.*;
import com.example.gamewebshop.dao.ProductDAO;
import com.example.gamewebshop.dto.ProductVariantDTOS.*;
import com.example.gamewebshop.models.Category;
import com.example.gamewebshop.models.CustomUser;
import com.example.gamewebshop.models.Product.*;
import com.example.gamewebshop.models.Product.Enums.Colors;
import com.example.gamewebshop.models.Product.Enums.Fit;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class AdminService {


    private final ProductDAO productDAO;
    private ProductRepository productRepository;
    private UserRepository userRepository;
    private BrandRepository brandRepository;
    private CategoryRepository categoryRepository;
    private CartProductRepository cartProductRepository;


    private ColorRepository colorRepository;
    private ProductImagesRepository productImagesRepository;

    private ProductVariantRepository productVariantRepository;

    private ProductVariatieRepository productVariatieRepository;
    private SizeAndFitRepository sizeAndFitRepository;
    private SizeRepository sizeRepository;


    public void createProductVariant(CreateProductVariantDTO createProductVariantDTO) {

        Product product = productDAO.getProductById(createProductVariantDTO.product.getId());
        Optional<SizeAndFit> sizeAndFit = sizeAndFitRepository.findById(Long.valueOf(createProductVariantDTO.sizeAndFit));
        Optional<Color> color = colorRepository.findById(Long.valueOf(createProductVariantDTO.color));

        if (sizeAndFit.isEmpty() || color.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No sizeAndFit or color found with that id"
            );
        }

        ProductVariant productVariant = new ProductVariant(product, createProductVariantDTO.description, color.get(), createProductVariantDTO.price, sizeAndFit.get());
        ProductImages productImages1 = new ProductImages(productVariant, createProductVariantDTO.imageUrl1);
        ProductImages productImages2 = new ProductImages(productVariant, createProductVariantDTO.imageUrl2);
        ProductImages productImages3 = new ProductImages(productVariant, createProductVariantDTO.imageUrl3);

        productRepository.save(product);
        productVariantRepository.save(productVariant);
        productImagesRepository.save(productImages1);
        productImagesRepository.save(productImages2);
        productImagesRepository.save(productImages3);


        for (ProductVariatieDTO productVariatieDTO : createProductVariantDTO.productVariatieDTOS) {
            Size size = new Size(productVariatieDTO.size, product.getCategory().getName());

            ProductVariatie productVariatie = new ProductVariatie(productVariant, size, productVariatieDTO.stock);
            sizeRepository.save(size);
            productVariatieRepository.save(productVariatie);
        }
    }


    public ResponseEntity<List<Product>> productsThatCanBeDeleted() {
        List<Product> products = productRepository.findAll();
        List<CartProduct> cartProducts = cartProductRepository.findAll();
        List<Product> productsThatCanBeDeleted = new ArrayList<>();

        for (Product product : products) {
            boolean canBeDeleted = true;
            for (ProductVariant productVariant : product.getProductVariants()) {
                for (CartProduct cartProduct : cartProducts) {
                    if (cartProduct.getProduct().getProductVariants().getFirst().getId() == productVariant.getId()) {

                        canBeDeleted = false;
                        break;
                    }
                }
                if (!canBeDeleted) {
                    break;
                }
            }
            if (canBeDeleted) {
                productsThatCanBeDeleted.add(product);
            }


    }
     return ResponseEntity.ok(productsThatCanBeDeleted);
}

   @Transactional
    public void deleteProductVariant(DeleteVariantDTO deleteVariantDTO) {

        Optional<Product> product = productRepository.findById(deleteVariantDTO.product.getId());

        if (product.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No product with that id"
            );
        }
         Product mainProduct = product.get();
        ProductVariant productVariant = mainProduct.getProductVariants().get(Integer.parseInt(deleteVariantDTO.productVariantIndex));
        log.info(String.valueOf(productVariant));

        List<ProductVariant> productVariants = mainProduct.getProductVariants();
        productVariants.remove(Integer.parseInt(deleteVariantDTO.productVariantIndex));

        productVariantRepository.deleteById(productVariant.getId());
        productRepository.save(mainProduct);

    }

    public void updateProductVariant(UpdateProductVariatieDTO updateProductVariatieDTO) {
        Optional<Product> product = productRepository.findById(updateProductVariatieDTO.product.getId());


        if (product.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No product with that id"
            );
        }

        long productVariant1Id =  product.get().getProductVariants().get(Integer.parseInt(updateProductVariatieDTO.productVariantIndex)).getId();
        Optional<ProductVariant> productVariant = productVariantRepository.findById(productVariant1Id);

        if (productVariant.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No product with that id"
            );
        }

        Optional<SizeAndFit> sizeAndFit = sizeAndFitRepository.findSizeAndFitByFit(Fit.valueOf(updateProductVariatieDTO.sizeAndFit));
        Optional<Color> color = colorRepository.findColorByName(Colors.valueOf(updateProductVariatieDTO.color.toUpperCase()));

        if (sizeAndFit.isEmpty() || color.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No sizeAndFit or color found with that id"
            );
        }

        ProductVariant productVariant1 = productVariant.get();
        productVariant1.setPrice(updateProductVariatieDTO.price);
        productVariant1.setDescription(updateProductVariatieDTO.description);
        productVariant1.setSize_and_fit(sizeAndFit.get());
        productVariant1.setColor(color.get());


        ProductImages productImages1 = productVariant1.getProductImages().getFirst();
        productImages1.setImageUrl(updateProductVariatieDTO.imageUrl1);
        ProductImages productImages2 = productVariant1.getProductImages().get(1);
        productImages2.setImageUrl(updateProductVariatieDTO.imageUrl2);
        productImagesRepository.save(productImages1);
        productImagesRepository.save(productImages2);

        productVariantRepository.save(productVariant1);

        updateProductVariatie(updateProductVariatieDTO.productVariatieDTOS, productVariant1, product.get());

    }

    private void updateProductVariatie(List<ProductVariatieDTO> productVariatieDTOS, ProductVariant productVariant1, Product product) {
        for (ProductVariatieDTO productVariatieDTO : productVariatieDTOS) {
            Optional<Size> size = sizeRepository.findSizeBySizeAndCategory(productVariatieDTO.size, product.getProductVariants().getFirst().getProductVariatie().getFirst().getSize().getCategory() );
            if (size.isEmpty()) {
                createNewVariation(productVariatieDTO, product.getProductVariants().getFirst().getProductVariatie().getFirst().getSize().getCategory(), productVariant1);
            } else {
                // Check if a product variation with the same size already exists
                Optional<ProductVariatie> existingVariatie = productVariant1.getProductVariatie()
                        .stream()
                        .filter(variatie -> variatie.getSize().equals(size.get()))
                        .findFirst();

                if (existingVariatie.isPresent()) {
                    // Update the quantity in stock of the existing product variation
                    ProductVariatie productVariatie = existingVariatie.get();
                    productVariatie.setQuantity_in_stock(productVariatieDTO.stock);
                    productVariatieRepository.save(productVariatie);
                }
                else {
                    createNewVariation(productVariatieDTO, product.getProductVariants().getFirst().getProductVariatie().getFirst().getSize().getCategory(), productVariant1);

                }
            }


        }
    }

    private void createNewVariation(ProductVariatieDTO productVariatieDTO, String category, ProductVariant productVariant) {

        Size newSize = new Size(productVariatieDTO.size, category);
        ProductVariatie productVariatienew = new ProductVariatie(productVariant, newSize, productVariatieDTO.stock);
        sizeRepository.save(newSize);
        productVariatieRepository.save(productVariatienew);

    }

    public void productionStop(ProductionStopDTO productionStopDTO) {

        Optional<Product> product = productRepository.findById(productionStopDTO.product.getId());
        if (product.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No product with that id"
            );
        }


        Optional<ProductVariatie> productVariatie = productVariatieRepository.findById(Long.valueOf(productionStopDTO.productVariatieIndex));

        if (productVariatie.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No productvariatie found with that id"
            );
        }
        ProductVariatie productVariatie1 = productVariatie.get();

        productVariatie1.setProductionStop(Boolean.valueOf(productionStopDTO.productionStop));

        productVariatieRepository.save(productVariatie1);


    }

    public void addStock(AddStockDTO addStockDTO) {

        Optional<Product> product = productRepository.findById(addStockDTO.product.getId());
        if (product.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No product with that id"
            );
        }



        Optional<ProductVariatie> productVariatie = productVariatieRepository.findById(Long.valueOf(addStockDTO.productVariatieIndex));

        if (productVariatie.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No productvariatie found with that id"
            );
        }
        ProductVariatie productVariatie1 = productVariatie.get();


       productVariatie1.setQuantity_in_stock(addStockDTO.stock);

        productVariatieRepository.save(productVariatie1);

    }

    public void createProduct(CreateProductDTO createProductDTO) {
        Optional<Brand> brandOptional = brandRepository.findById(Long.valueOf(createProductDTO.brandId));
        if (brandOptional.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No brand found with that id"
            );
        }
        Optional<Category> categoryOptional = categoryRepository.findById(Long.valueOf(createProductDTO.categoryId));
        if (categoryOptional.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No Category found with that id"
            );
        }

        Brand brand = brandOptional.get();
        Category category = categoryOptional.get();

        Product product = new Product(brand, createProductDTO.name, createProductDTO.country, category);
        productRepository.save(product);
    }

    public ResponseEntity<List<CustomUser>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}
