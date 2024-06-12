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

    public String validatePromocode(String promoCode, double totalPrice){

        double discount = 0.0;

        String effectivePromoCode = promoCode;

        if (effectivePromoCode != null && !effectivePromoCode.isEmpty()) {
            Optional<PromoCode> promoCodeOptional = promoCodeDAO.getPromoCodeByCode(effectivePromoCode);
            if (promoCodeOptional.isPresent() && promoCodeDAO.isPromoCodeValid(effectivePromoCode)) {
                PromoCode code = promoCodeOptional.get();
                if (totalPrice >= code.getMinSpendAmount()) {
                    discount = calculateDiscount(totalPrice, code);

                    code.setMaxUsageCount(code.getMaxUsageCount() - 1);
                    code.setUsageCount(code.getUsageCount() + 1);
                    code.setTotalDiscountAmount(code.getTotalDiscountAmount() + discount);
                    promoCodeRepository.save(code);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Total price does not meet the minimum spend amount for this promo code");
                }
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired promo code");
            }
        } else if (totalPrice > 120) {
            Optional<PromoCode> autoDiscountPromo = promoCodeDAO.getPromoCodeByCode("AUTO_DISCOUNT");
            if (autoDiscountPromo.isPresent() && promoCodeDAO.isPromoCodeValid("AUTO_DISCOUNT")) {
                PromoCode autoDiscountCode = autoDiscountPromo.get();
                discount = calculateDiscount(totalPrice, autoDiscountCode);

                autoDiscountCode.setMaxUsageCount(autoDiscountCode.getMaxUsageCount() - 1);
                autoDiscountCode.setUsageCount(autoDiscountCode.getUsageCount() + 1);
                autoDiscountCode.setTotalDiscountAmount(autoDiscountCode.getTotalDiscountAmount() + discount);
                promoCodeRepository.save(autoDiscountCode);
                effectivePromoCode = "AUTO_DISCOUNT";
            }
        }
        return effectivePromoCode;
    }

    public void validateGiftcard(String giftCardCode){

        if (giftCardCode != null && !giftCardCode.isEmpty()) {
            Optional<Giftcard> giftcardOptional = giftcardRepository.findByCode(giftCardCode);
            if (giftcardOptional.isPresent() && !giftcardOptional.get().isUsed()) {
                Giftcard giftcard = giftcardOptional.get();

                giftcard.setUsed(true);
                giftcardRepository.save(giftcard);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or used gift card code");
            }
        }
    }


    @Transactional
    public void saveOrderWithProducts(OrderDTO orderDTO, String userEmail) {
        CustomUser user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        }
        PlacedOrder order = new PlacedOrder();


        long totalProducts = 0L;
        List<CartProduct> cartproducts = validateCartproductIds(orderDTO.cartProductId);

        for (CartProduct cartProduct: cartproducts){
            totalProducts += cartProduct.getQuantity();
        }

        double totalPrice = calculateTotalPrice(cartproducts);

        changeCartProductStatusAndStock(cartproducts);
        validateGiftcard(orderDTO.giftCardCode);
        String effectivePromocode =  validatePromocode(orderDTO.promoCode, totalPrice);

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
        order.setDiscountedPrice(orderDTO.discountedPrice);
        order.setPromoCode(effectivePromocode);
        order.setGiftCardCode(orderDTO.giftCardCode);
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);
    }

    public void changeCartProductStatusAndStock(List<CartProduct> cartproducts){
        for (CartProduct cartProduct : cartproducts) {
            cartProduct.setStatus(CartProductStatus.Ordered);
            productDAO.changeStockOfOrderedProducts(cartProduct.getProduct(), cartProduct.getQuantity(), cartProduct.getImageUrl(), cartProduct.getSize());
            cartProductRepository.save(cartProduct);

        }
    }

    public List<CartProduct> validateCartproductIds(List<Long> cartproductIds){
        List<CartProduct> cartproducts = new ArrayList<>();

        for (Long id: cartproductIds){
            Optional<CartProduct> cartProduct = cartProductRepository.findById(id);

            if (cartProduct.isEmpty()){
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No cartproduct found with that id"
                );
            }
            cartproducts.add(cartProduct.get());
        }
        return cartproducts;
    }


    public double calculateTotalPrice(List<CartProduct> cartProducts) {
        long totalPrice = 0;
        for (CartProduct cartProduct: cartProducts){
            totalPrice += cartProduct.getPrice();
        }
        return totalPrice;
    }

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
            orderUserDTO.totalPrice = order.getTotalPrice();
            orderUserDTO.discountedPrice = order.getDiscountedPrice();;
            orderUserDTO.promoCode = order.getPromoCode();
            for (CartProduct cartProduct: order.getCartProducts())
            {
                OrderRetrievalDTO orderRetrievalDTO = new OrderRetrievalDTO();
                orderRetrievalDTO.name = cartProduct.getProduct().getName();
                orderRetrievalDTO.imageUrl = cartProduct.getImageUrl();
                orderRetrievalDTO.price = cartProduct.getPrice();
                orderRetrievalDTO.productVariantPrice = cartProduct.getProductVariantPrice();
                orderRetrievalDTO.size = cartProduct.getSize();
                orderRetrievalDTO.quantity = cartProduct.getQuantity();
                orderRetrievalDTO.cartproductId = cartProduct.getId();
                orderRetrievalDTO.productReturned = cartProduct.isProductReturned();
                orderRetrievalDTO.returnStatus = cartProduct.getReturnStatus();
                orderUserDTO.cartProducts.add(orderRetrievalDTO);
            }

            orderUserDTOS.add(orderUserDTO);
        }

        return orderUserDTOS;
    }

    public double calculateDiscount(double totalPrice, PromoCode promoCode) {
        if (promoCode.getType() == PromoCode.PromoCodeType.FIXED_AMOUNT) {
            return promoCode.getDiscount();
        } else if (promoCode.getType() == PromoCode.PromoCodeType.PERCENTAGE) {
            return totalPrice * (promoCode.getDiscount() / 100);
        }
        return 0.0;
    }

    public List<OrderUserDTO> getOrdersByUserIdForDashboard(CustomUser customUser) {

        return  getOrdersByUserId(customUser);
    }

}



