package com.example.gamewebshop.dao;

import com.example.gamewebshop.Repositorys.CategoryRepository;
import com.example.gamewebshop.Repositorys.ProductRepository;
import com.example.gamewebshop.Repositorys.ProductVariatieRepository;
import com.example.gamewebshop.dto.ProductVariantDTOS.ProductByIdDTO;
import com.example.gamewebshop.models.Product.Product;
import com.example.gamewebshop.models.Product.ProductVariant;
import com.example.gamewebshop.models.PromoCode;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductDAOTest {

    private ProductDAO productDAO;
    @Mock
    private  ProductRepository productRepository;
    @Mock
    private  CategoryRepository categoryRepository;
    @Mock
    private  ProductVariatieRepository productVariatieRepository;
    @Mock
    private  PromoCodeDAO promoCodeDAO;


    @BeforeEach
    void setUp() {
        productDAO = new ProductDAO(productRepository, categoryRepository, productVariatieRepository, promoCodeDAO);
    }

    @Test
    void should_return_correct_product_data_when_converting_product_to_productByIdDTO() {

        // arrange
        long categoryId = 1L;
        Product product = new Product();
        ProductVariant productVariant1 = new ProductVariant();
        ProductVariant productVariant2 = new ProductVariant();
        product.setProductVariants(List.of(productVariant2, productVariant1));
        product.setCountry("Oegenbourg");
        product.setId(1);
        product.setQuantity(3);
        product.setName("test watch");
        PromoCode promoCode = new PromoCode();
        promoCode.setCode("test1234");
        promoCode.setType(PromoCode.PromoCodeType.FIXED_AMOUNT);
        promoCode.setDiscount(1000);

        // stubs
        when(this.promoCodeDAO.getPromoCodeByCategory(categoryId)).thenReturn(Optional.of(promoCode));

        // act
        ProductByIdDTO result = productDAO.productByIdDTOconverter(categoryId, product);
        // assert
        assertThat(result.categoryId).isEqualTo(categoryId);


    }

    @Test void should_throw_http_status_Not_Found_Exception_when_finding_product_with_non_existing_id(){
        // arrange
         long productId = 20L;
         String expectedErrorMesage = "404 NOT_FOUND \"No products found with that id\"";

        // stubs
        when(this.productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> productDAO.getProductById(productId));

        // assert
        MatcherAssert.assertThat(exception.getMessage(), is(expectedErrorMesage));



    }
}