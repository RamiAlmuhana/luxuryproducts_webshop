package com.example.gamewebshop.services;

import com.example.gamewebshop.Repositorys.*;
import com.example.gamewebshop.dao.ProductDAO;
import com.example.gamewebshop.dto.ProductVariantDTOS.CreateProductVariantDTO;
import com.example.gamewebshop.dto.ProductVariantDTOS.ProductVariatieDTO;
import com.example.gamewebshop.dto.ProductVariantDTOS.ProductionStopDTO;
import com.example.gamewebshop.models.Category;
import com.example.gamewebshop.models.Product.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    private AdminService adminService;
    @Mock
    private  ProductDAO productDAO;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BrandRepository brandRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CartProductRepository cartProductRepository;
    @Mock
    private ColorRepository colorRepository;
    @Mock
    private ProductImagesRepository productImagesRepository;
    @Mock
    private ProductVariantRepository productVariantRepository;
    @Mock
    private ProductVariatieRepository productVariatieRepository;
    @Mock
    private SizeAndFitRepository sizeAndFitRepository;
    @Mock
    private SizeRepository sizeRepository;

    @BeforeEach
    void setUp() {
        adminService = new AdminService(productDAO, productRepository, userRepository, brandRepository, categoryRepository, cartProductRepository, colorRepository
        , productImagesRepository, productVariantRepository, productVariatieRepository, sizeAndFitRepository, sizeRepository);
    }

    @Test
    void should_update_productvariatie_productionStop_to_true_when_value_true_is_given() {

        // arrange
        ProductionStopDTO productionStopDTO = new ProductionStopDTO();
        productionStopDTO.productionStop = "true";
        productionStopDTO.productVariatieIndex = "2";
        productionStopDTO.product = new Product();
        productionStopDTO.product.setId(1);
        Boolean expectedValue = true;

        // stubs
        when(this.productRepository.findById(anyLong())).thenReturn(Optional.of(new Product()));
        when(this.productVariatieRepository.findById(anyLong())).thenReturn(Optional.of(new ProductVariatie()));

        // act
        adminService.productionStop(productionStopDTO);

        // assert(comparator)
        ArgumentCaptor<ProductVariatie> productVariatieArgumentCaptor = ArgumentCaptor.forClass(ProductVariatie.class);
        verify(this.productVariatieRepository).save(productVariatieArgumentCaptor.capture());
        ProductVariatie capturedProductVariatie = productVariatieArgumentCaptor.getValue();
        assertThat(capturedProductVariatie.getProductionStop()).isEqualTo(expectedValue);


    }


    @Test
    void should_create_ProductVariant_when_correct_data_is_added() {
        // arrange
        Brand brand = new Brand("Sample Brand", "Sample brand description");
        Category category = new Category();
        category.setName("Sample Category");

        Product product = new Product();
        product.setId(1);
        product.setBrand(brand);
        product.setName("Sample Product");
        product.setCountry("Sample Country");
        product.setCategory(category);

        ProductVariant productVariant = new ProductVariant();
        ProductVariatie productVariatie = new ProductVariatie();
        Size size = new Size();
        size.setCategory("test_Watches");

        productVariatie.setSize(size);
        productVariant.setProductVariatie(List.of(productVariatie));
        product.setProductVariants(List.of(productVariant));

        CreateProductVariantDTO createProductVariantDTO = new CreateProductVariantDTO();
        createProductVariantDTO.product = product;
        createProductVariantDTO.description = "Sample description";
        createProductVariantDTO.price = 1999;
        createProductVariantDTO.color = "1";
        createProductVariantDTO.sizeAndFit = "1";
        createProductVariantDTO.imageUrl1 = "https://example.com/image1.jpg";
        createProductVariantDTO.imageUrl2 = "https://example.com/image2.jpg";
        createProductVariantDTO.imageUrl3 = "https://example.com/image3.jpg";
        ProductVariatieDTO productVariatieDTO = new ProductVariatieDTO();
        productVariatieDTO.stock = 10;
        productVariatieDTO.size = "1";
        createProductVariantDTO.productVariatieDTOS = new ArrayList<>();
        createProductVariantDTO.productVariatieDTOS.add(productVariatieDTO);

        // stubs
        when(this.productDAO.getProductById(anyLong())).thenReturn(product);
        when(this.sizeAndFitRepository.findById(anyLong())).thenReturn(Optional.of(new SizeAndFit()));
        when(this.colorRepository.findById(anyLong())).thenReturn(Optional.of(new Color()));


        // act
        adminService.createProductVariant(createProductVariantDTO);

        // assert
        ArgumentCaptor<ProductVariant> productVariantArgumentCaptor = ArgumentCaptor.forClass(ProductVariant.class);
        verify(this.productVariantRepository).save(productVariantArgumentCaptor.capture());
        ProductVariant capturedProductVariant = productVariantArgumentCaptor.getValue();
        assertThat(capturedProductVariant.getProduct().getName()).isEqualTo(createProductVariantDTO.product.getName());
    }


}