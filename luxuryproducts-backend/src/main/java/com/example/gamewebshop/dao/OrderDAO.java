package com.example.gamewebshop.dao;

import com.example.gamewebshop.models.CustomUser;
import com.example.gamewebshop.models.PlacedOrder;
import com.example.gamewebshop.models.Product;
import com.example.gamewebshop.models.PromoCode;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Component
public class OrderDAO {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PromoCodeRepository promoCodeRepository;
    private final PromoCodeDAO promoCodeDAO;

    public OrderDAO(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository, PromoCodeRepository promoCodeRepository, PromoCodeDAO promoCodeDAO) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.promoCodeRepository = promoCodeRepository;
        this.promoCodeDAO = promoCodeDAO;
    }

    public List<PlacedOrder> getAllOrders() {
        return this.orderRepository.findAll();
    }

    @Transactional
    public void createOrder(PlacedOrder placedOrder, String userEmail, String promoCode) {
        CustomUser user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        }
        placedOrder.setUser(user);

        HashSet<Product> productsWithCategory = new HashSet<>();
        for (Product product : placedOrder.getProducts()) {
            Product foundProduct = productRepository.findById(product.getId()).orElse(null);
            if (foundProduct == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product not found");
            }
            productsWithCategory.add(foundProduct);
        }
        placedOrder.setProducts(productsWithCategory);

        double totalPrice = calculateTotalPrice(placedOrder);
        double discountedPrice = totalPrice;
        double discount = 0.0;

        String effectivePromoCode = promoCode != null && !promoCode.isEmpty() ? promoCode : placedOrder.getPromoCode();

        if (effectivePromoCode != null && !effectivePromoCode.isEmpty()) {
            Optional<PromoCode> promoCodeOptional = promoCodeDAO.getPromoCodeByCode(effectivePromoCode);
            if (promoCodeOptional.isPresent() && promoCodeDAO.isPromoCodeValid(effectivePromoCode)) {
                PromoCode code = promoCodeOptional.get();
                if (totalPrice >= code.getMinSpendAmount()) {
                    discount = calculateDiscount(totalPrice, code);
                    discountedPrice -= discount;
                    if (discountedPrice < 0) {
                        discountedPrice = 0;
                    }
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
            // Try to apply AUTO_DISCOUNT promo code
            Optional<PromoCode> autoDiscountPromo = promoCodeDAO.getPromoCodeByCode("AUTO_DISCOUNT");
            if (autoDiscountPromo.isPresent() && promoCodeDAO.isPromoCodeValid("AUTO_DISCOUNT")) {
                PromoCode autoDiscountCode = autoDiscountPromo.get();
                discount = calculateDiscount(totalPrice, autoDiscountCode);
                discountedPrice -= discount;
                if (discountedPrice < 0) {
                    discountedPrice = 0;
                }
                autoDiscountCode.setMaxUsageCount(autoDiscountCode.getMaxUsageCount() - 1);
                autoDiscountCode.setUsageCount(autoDiscountCode.getUsageCount() + 1);
                autoDiscountCode.setTotalDiscountAmount(autoDiscountCode.getTotalDiscountAmount() + discount);
                promoCodeRepository.save(autoDiscountCode);
                effectivePromoCode = "AUTO_DISCOUNT";
            }
        }

        placedOrder.setTotalPrice(totalPrice);
        placedOrder.setDiscountedPrice(discountedPrice);
        placedOrder.setPromoCode(effectivePromoCode);
        orderRepository.save(placedOrder);
    }

    public List<PlacedOrder> getOrdersByUserId(long userId) {
        Optional<List<PlacedOrder>> orderList = this.orderRepository.findByUserId(userId);
        if (orderList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No orders found for this user");
        }
        return orderList.get();
    }

    public double calculateTotalPrice(PlacedOrder placedOrder) {
        return placedOrder.getProducts().stream().mapToDouble(Product::getPrice).sum();
    }

    public double calculateDiscount(double totalPrice, PromoCode promoCode) {
        if (promoCode.getType() == PromoCode.PromoCodeType.FIXED_AMOUNT) {
            return promoCode.getDiscount();
        } else if (promoCode.getType() == PromoCode.PromoCodeType.PERCENTAGE) {
            return totalPrice * (promoCode.getDiscount() / 100);
        }
        return 0.0;
    }
}
