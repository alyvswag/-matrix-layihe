package org.example.demo13213.service.order;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.demo13213.exception.BaseException;
import org.example.demo13213.model.dao.*;
import org.example.demo13213.model.dto.enums.order.OrderStatus;
import org.example.demo13213.repo.cart.CartItemRepo;
import org.example.demo13213.repo.product.ProductInventoryRepo;
import org.example.demo13213.repo.order.OrderItemRepo;
import org.example.demo13213.repo.order.OrderRepo;
import org.example.demo13213.repo.user.UserRepo;
import org.example.demo13213.security.UserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.example.demo13213.model.dto.enums.response.ErrorResponseMessages.CART_EMPTY;
import static org.example.demo13213.model.dto.enums.response.ErrorResponseMessages.PRODUCT_OUT_OF_STOCK;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    final CartItemRepo cartItemRepo;
    final UserRepo userRepo;
    final OrderRepo orderRepo;
    final OrderItemRepo orderItemsRepo;
    final ProductInventoryRepo productInventoryRepo;

    @Override
    @Transactional
    public Orders checkoutOrder() {
        // 1) Aktiv user-i götür
        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        // 2) User-in səbətindəki məhsulları tap
        List<CartItems> cartItems = cartItemRepo.findByUserIdForCartItem(user.getId());
        if (cartItems.isEmpty()) {
            throw BaseException.of(CART_EMPTY);//exception atdiq
        }

        // 3) User entity-ni tap
        Users u = userRepo.findUserByUsername(user.getUsername())
                .orElseThrow(() -> {
                    log.error("❌ User not found: username={}", user.getUsername());
                    return BaseException.notFound(Users.class.getSimpleName(), "username", user.getUsername());
                });

        // 4) Orders cədvəlində ilkin order yarat
        Orders order = new Orders();
        order.setUser(u);
        order.setStatus(OrderStatus.PENDING);

        // şimdilik shipping fix 5 AZN kimi
        order.setShippingFee(BigDecimal.valueOf(5));
        orderRepo.save(order);

        int totalItems = 0;
        BigDecimal subtotal = BigDecimal.ZERO;

        // 5) Hər CartItem üçün OrderItem yarat, stokdan çıx, total-ları hesabla
        for (CartItems cartItem : cartItems) {
            Products product = cartItem.getProduct();

            // quantity field-i CartItems entity-nə əlavə etdiyini fərz edirəm:
            // private Integer quantity;
            Integer requestedQty = cartItem.getQuantity();

            // 5.1) Stoku yoxla
            ProductInventory inventory = productInventoryRepo.findById(product.getId())
                    .orElseThrow(() -> {
                        log.error("❌ Inventory not found for productId={}", product.getId());
                        return BaseException.notFound(ProductInventory.class.getSimpleName(),
                                "productId", String.valueOf(product.getId()));
                    });

            if (inventory.getQuantity() < requestedQty) {
                log.error("❌ Product out of stock: productId={}, requested={}, available={}",
                        product.getId(), requestedQty, inventory.getQuantity());
                throw BaseException.of(PRODUCT_OUT_OF_STOCK);
            }

            // 5.2) Qiymət hesabla
            BigDecimal unitPrice = product.getPrice();
            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(requestedQty));

            // 5.3) OrderItems yaz
            OrderItems orderItem = new OrderItems();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(requestedQty);
            orderItem.setUnitPrice(unitPrice);
            orderItem.setLineTotal(lineTotal);

            orderItemsRepo.save(orderItem);

            // 5.4) Toplamları yığ
            subtotal = subtotal.add(lineTotal);
            totalItems += requestedQty;

            // 5.5) Stokdan çıx
            inventory.setQuantity(inventory.getQuantity() - requestedQty);
            productInventoryRepo.save(inventory);
        }

        // 6) Order-in total-larını set et
        order.setTotalItems(totalItems);
        order.setSubtotal(subtotal);
        // hələ ki endirim yoxdur
        order.setDiscountTotal(BigDecimal.ZERO);

        BigDecimal grandTotal = subtotal
                .subtract(order.getDiscountTotal())
                .add(order.getShippingFee());
        order.setGrandTotal(grandTotal);

        orderRepo.save(order);

        // 7) Səbəti təmizlə
        cartItemRepo.deleteAll(cartItems);

        return order;
    }
}
