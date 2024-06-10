package com.example.gamewebshop.dao;

import com.example.gamewebshop.Repositorys.*;
import com.example.gamewebshop.dto.ProductVariantDTOS.OrderDTO;
import com.example.gamewebshop.dto.ProductVariantDTOS.OrderRetrievalDTO;
import com.example.gamewebshop.dto.ProductVariantDTOS.OrderUserDTO;
import com.example.gamewebshop.models.CustomUser;
import com.example.gamewebshop.models.Giftcard;
import com.example.gamewebshop.models.PlacedOrder;
import com.example.gamewebshop.models.Product.CartProduct;
import com.example.gamewebshop.models.Product.Enums.CartProductStatus;
import com.example.gamewebshop.models.Product.Product;
import com.example.gamewebshop.models.PromoCode;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
@AllArgsConstructor
@Component
public class OrderDAO {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductDAO productDAO;
    private final PromoCodeRepository promoCodeRepository;
    private final PromoCodeDAO promoCodeDAO;
    private final GiftcardRepository giftcardRepository;
    private final CartProductRepository cartProductRepository;



    public List<PlacedOrder> getAllOrders() {
        return this.orderRepository.findAll();
    }

//    @Transactional
//    public void createOrder(PlacedOrder placedOrder, String userEmail, String promoCode, String giftCardCode) {
//        CustomUser user = userRepository.findByEmail(userEmail);
//        if (user == null) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
//        }
//        placedOrder.setUser(user);
//
//        HashSet<Product> productsWithCategory = new HashSet<>();
//        for (Product product : placedOrder.getProducts()) {
//            Product foundProduct = productRepository.findById(product.getId()).orElse(null);
//            if (foundProduct == null) {
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product not found");
//            }
//            productsWithCategory.add(foundProduct);
//        }
//        placedOrder.setProducts(productsWithCategory);
//
//        double totalPrice = calculateTotalPrice(placedOrder);
//        double discount = 0.0;
//
//        String effectivePromoCode = promoCode != null && !promoCode.isEmpty() ? promoCode : placedOrder.getPromoCode();
//
//        if (effectivePromoCode != null && !effectivePromoCode.isEmpty()) {
//            Optional<PromoCode> promoCodeOptional = promoCodeDAO.getPromoCodeByCode(effectivePromoCode);
//            if (promoCodeOptional.isPresent() && promoCodeDAO.isPromoCodeValid(effectivePromoCode)) {
//                PromoCode code = promoCodeOptional.get();
//                if (totalPrice >= code.getMinSpendAmount()) {
//                    discount = calculateDiscount(totalPrice, code);
////                    discountedPrice -= discount;
////                    if (discountedPrice < 0) {
////                        discountedPrice = 0;
////                    }
//                    code.setMaxUsageCount(code.getMaxUsageCount() - 1);
//                    code.setUsageCount(code.getUsageCount() + 1);
//                    code.setTotalDiscountAmount(code.getTotalDiscountAmount() + discount);
//                    promoCodeRepository.save(code);
//                } else {
//                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Total price does not meet the minimum spend amount for this promo code");
//                }
//            } else {
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired promo code");
//            }
//        } else if (totalPrice > 120) {
//            Optional<PromoCode> autoDiscountPromo = promoCodeDAO.getPromoCodeByCode("AUTO_DISCOUNT");
//            if (autoDiscountPromo.isPresent() && promoCodeDAO.isPromoCodeValid("AUTO_DISCOUNT")) {
//                PromoCode autoDiscountCode = autoDiscountPromo.get();
//                discount = calculateDiscount(totalPrice, autoDiscountCode);
////                discountedPrice -= discount;
////                if (discountedPrice < 0) {
////                    discountedPrice = 0;
////                }
//                autoDiscountCode.setMaxUsageCount(autoDiscountCode.getMaxUsageCount() - 1);
//                autoDiscountCode.setUsageCount(autoDiscountCode.getUsageCount() + 1);
//                autoDiscountCode.setTotalDiscountAmount(autoDiscountCode.getTotalDiscountAmount() + discount);
//                promoCodeRepository.save(autoDiscountCode);
//                effectivePromoCode = "AUTO_DISCOUNT";
//            }
//        }
//
//        if (giftCardCode != null && !giftCardCode.isEmpty()) {
//            Optional<Giftcard> giftcardOptional = giftcardRepository.findByCode(giftCardCode);
//            if (giftcardOptional.isPresent() && !giftcardOptional.get().isUsed()) {
//                Giftcard giftcard = giftcardOptional.get();
////                discountedPrice -= giftcard.getDiscountAmount();
////                if (discountedPrice < 0) {
////                    discountedPrice = 0;
////                }
//                giftcard.setUsed(true);
//                giftcardRepository.save(giftcard);
//                placedOrder.setGiftCardCode(giftCardCode);
//            } else {
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or used gift card code");
//            }
//        }
//
//        placedOrder.setTotalPrice(totalPrice);
////        placedOrder.setDiscountedPrice(totalPrice);
//        placedOrder.setPromoCode(effectivePromoCode);
//        placedOrder.setGiftCardCode(giftCardCode);
//        orderRepository.save(placedOrder);
//    }

    public List<OrderUserDTO> getOrdersByUserId(CustomUser customUser){
        Optional<List<PlacedOrder>> orderList = this.orderRepository.findAllByUser(customUser);
        if (orderList.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No products found with that user"
            );
        }
        List<OrderUserDTO> orderUserDTOS = new ArrayList<>();
        for (PlacedOrder order: orderList.get()){
            OrderUserDTO orderUserDTO = new OrderUserDTO();
            orderUserDTO.orderDate = order.getOrderDate();
            orderUserDTO.houseNumber = order.getHouseNumber();
            orderUserDTO.user = order.getUser();
            orderUserDTO.name = order.getName();
            orderUserDTO.infix = order.getInfix();
            orderUserDTO.last_name = order.getLast_name();
            orderUserDTO.notes = order.getNotes();
            orderUserDTO.totalProducts = order.getTotalProducts();
            orderUserDTO.zipcode = order.getZipcode();
            for (CartProduct cartProduct: order.getCartProducts())
            {
                OrderRetrievalDTO orderRetrievalDTO = new OrderRetrievalDTO();
                orderRetrievalDTO.name = cartProduct.getProduct().getName();
                orderRetrievalDTO.imageUrl = cartProduct.getImageUrl();
                orderRetrievalDTO.price = cartProduct.getPrice();
                orderRetrievalDTO.productVariantPrice = cartProduct.getProductVariantPrice();
                orderRetrievalDTO.size = cartProduct.getSize();
                orderRetrievalDTO.quantity = cartProduct.getQuantity();
                orderUserDTO.cartProducts.add(orderRetrievalDTO);
            }

            orderUserDTOS.add(orderUserDTO);
        }

        return orderUserDTOS;
    }


    @Transactional
    public void saveOrderWithProducts(OrderDTO orderDTO, String userEmail) {
        CustomUser user = userRepository.findByEmail(userEmail);
        PlacedOrder order = new PlacedOrder();


        Long totalProducts = 0L;
        List<CartProduct> cartproducts = new ArrayList<>();

        for (Long id: orderDTO.cartProductId){
            Optional<CartProduct> cartProduct = cartProductRepository.findById(id);

            if (cartProduct.isEmpty()){
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No cartproduct found with that id"
                );
            }
            cartproducts.add(cartProduct.get());
        }

        for (CartProduct cartProduct: cartproducts){
            totalProducts += cartProduct.getQuantity();
        }




        for (CartProduct cartProduct : cartproducts) {
            cartProduct.setStatus(CartProductStatus.Ordered);
            productDAO.changeStockOfOrderedProducts(cartProduct.getProduct(), cartProduct.getQuantity(), cartProduct.getImageUrl(), cartProduct.getSize());
            cartProductRepository.save(cartProduct);

        }
        order.setUser(user);
        order.setTotalProducts(Math.toIntExact(totalProducts));
        order.setOrderDate(LocalDateTime.now());
        order.setCartProducts(cartproducts);
        order.setName(orderDTO.name);
        order.setInfix(orderDTO.infix);
        order.setLast_name(orderDTO.last_name);
        order.setNotes(orderDTO.notes);
        order.setHouseNumber(orderDTO.houseNumber);
        order.setZipcode(orderDTO.zipcode);




        orderRepository.save(order);
    }


//    public double calculateTotalPrice(PlacedOrder placedOrder) {
//        return placedOrder.getProducts().stream().mapToDouble(Product::getPrice).sum();
//    }

    public double calculateDiscount(double totalPrice, PromoCode promoCode) {
        if (promoCode.getType() == PromoCode.PromoCodeType.FIXED_AMOUNT) {
            return promoCode.getDiscount();
        } else if (promoCode.getType() == PromoCode.PromoCodeType.PERCENTAGE) {
            return totalPrice * (promoCode.getDiscount() / 100);
        }
        return 0.0;
    }




}
