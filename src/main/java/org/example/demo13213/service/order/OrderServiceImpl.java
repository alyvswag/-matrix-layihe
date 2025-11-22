package org.example.demo13213.service.order;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.demo13213.exception.BaseException;
import org.example.demo13213.model.dao.*;
import org.example.demo13213.model.dto.enums.order.OrderStatus;
import org.example.demo13213.repo.cart.CartItemRepo;
import org.example.demo13213.repo.coupon.CouponRepo;
import org.example.demo13213.repo.product.ProductInventoryRepo;
import org.example.demo13213.repo.order.OrderItemRepo;
import org.example.demo13213.repo.order.OrderRepo;
import org.example.demo13213.repo.user.UserRepo;
import org.example.demo13213.security.UserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.example.demo13213.model.dto.enums.order.OrderStatus.CANCELLED;
import static org.example.demo13213.model.dto.enums.order.OrderStatus.PAID;
import static org.example.demo13213.model.dto.enums.response.ErrorResponseMessages.*;

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
    final CouponRepo couponRepo;

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

        //  shipping fix 5 AZN kimi
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
        //todo: sebebtin temizlenmemesi ucun asagidaki setri commente at
        cartItemRepo.deleteAll(cartItems);

        return order;
    }

    @Override
    public void confirmPayment(Long id) {
        Orders order = orderRepo.findByIdForOrders(id)
                .orElseThrow(() -> {
            log.error("❌ Order not found. orderId={}", id);
            return BaseException.notFound("orders", id.toString(), id);
        });
        order.setStatus(PAID);
        orderRepo.save(order);
        //orderitem cedvelinde orderid ye uygun olan columnlari temizlemeliyik
    }

    @Override
    public void cancelPayment(Long id) {
        Orders order = orderRepo.findByIdForOrders(id)
                .orElseThrow(() -> {
                    log.error("❌ Order not found. orderId={}", id);
                    return BaseException.notFound("orders", id.toString(), id);
                });
        order.setStatus(CANCELLED);
        //indi ise product inventoryden mehsulun sayi cixilir
        //ilk once List orderitemsden order id ye uygun olan order itemslari getirirem
        //sora for dovru qurub hemin for dovrunde sira sira
        List<OrderItems> orderItems = orderItemsRepo.findByOrderItemForOrderId(id);
        if (orderItems.isEmpty()) {
            throw BaseException.of(ORDER_EMPTY);//exception atdiq
        }
        for (OrderItems orderItem : orderItems) {
            ProductInventory inventory = productInventoryRepo.findById(orderItem.getProduct().getId())
                    .orElseThrow(() -> {
                        return BaseException.notFound(ProductInventory.class.getSimpleName(),
                                "productId", String.valueOf(orderItem.getProduct().getId()));
                    });
            inventory.setQuantity(inventory.getQuantity()+orderItem.getQuantity());
            productInventoryRepo.save(inventory);
        }
        orderRepo.save(order);
    }

    @Override
    public List<OrderItems> myOrders() {
        // 1) Aktiv user-i götür
        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Users u = userRepo.findUserByUsername(user.getUsername())
                .orElseThrow(() -> {
                    log.error("❌ User not found: username={}", user.getUsername());
                    return BaseException.notFound(Users.class.getSimpleName(), "username", user.getUsername());
                });
        List<Orders> orders = orderRepo.findOrdersByUserId(u.getId());
        List<OrderItems> orderItems = new ArrayList<OrderItems>();
        for(Orders order : orders) {
            List<OrderItems> oi = orderItemsRepo.findByOrderItemForOrderId(order.getId());
            orderItems.addAll(oi);
        }
        return orderItems;
    }
    @Transactional
    public void applyCouponForOrders(String couponCode, Long orderId) {
        // 1. Kuponu tapırıq
        Coupons coupon = couponRepo.findCoupons(couponCode, true)
                .orElseThrow(() -> {
                    log.error("❌ Coupon not found or inactive. code={}", couponCode);
                    return BaseException.notFound("coupon", "code", couponCode);
                });

        // 2. Order-i tapırıq (burda səndə id yazılmışdı, onu orderId ilə düzəldirəm)
        Orders order = orderRepo.findByIdForOrders(orderId)
                .orElseThrow(() -> {
                    log.error("❌ Order not found. orderId={}", orderId);
                    return BaseException.notFound("orders", "id", orderId.toString());
                });

        // 3. Bu order-ə aid bütün OrderItems
        List<OrderItems> orderItems = orderItemsRepo.findByOrderItemForOrderId(orderId);
        if (orderItems.isEmpty()) {
            throw BaseException.of(ORDER_EMPTY);
        }

        // 4. Coupon məlumatları
        if (coupon.getCategory() == null || coupon.getCategory().getId() == null) {
            // Bu halda kupon hansı kategoriya üçün keçərlidir bilinmir
            // istəsən burada ayrıca exception ata bilərsən
            log.error("❌ Coupon category is null. couponId={}", coupon.getId());
            throw BaseException.of(COUPON_NOT_APPLICABLE);
        }

        Long couponCategoryId = coupon.getCategory().getId();
        BigDecimal couponDiscount = coupon.getDiscountValue();

        if (couponDiscount == null || couponDiscount.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("❌ Coupon discount value is invalid. couponId={}", coupon.getId());
            throw BaseException.of(COUPON_NOT_APPLICABLE);
        }

        // Bu order üçün ümumi endirim və yeni subtotal hesablayacağıq
        BigDecimal totalDiscountForOrder = BigDecimal.ZERO;
        BigDecimal newSubtotal = BigDecimal.ZERO;

        for (OrderItems item : orderItems) {
            Products product = item.getProduct();

            if (product == null ||
                    product.getCategory() == null ||
                    product.getCategory().getId() == null) {
                // Məhsulda kategoriya yoxdursa, endirim tətbiq etmirik
                newSubtotal = newSubtotal.add(item.getLineTotal());
                continue;
            }

            // Bu item kuponun kategoriyasına aid deyil → endirim yox
            if (!product.getCategory().getId().equals(couponCategoryId)) {
                newSubtotal = newSubtotal.add(item.getLineTotal());
                continue;
            }

            // Kupon tətbiq olunan məhsullar
            BigDecimal currentUnitPrice = item.getUnitPrice();
            if (currentUnitPrice == null) {
                currentUnitPrice = product.getPrice(); // fallback
            }

            // Unit price-dan kupon dəyərini çıxırıq
            BigDecimal discountedUnitPrice = currentUnitPrice.subtract(couponDiscount);
            if (discountedUnitPrice.compareTo(BigDecimal.ZERO) < 0) {
                discountedUnitPrice = BigDecimal.ZERO;
            }

            BigDecimal originalLineTotal = item.getLineTotal();
            if (originalLineTotal == null) {
                originalLineTotal = currentUnitPrice.multiply(
                        BigDecimal.valueOf(item.getQuantity())
                );
            }

            BigDecimal newLineTotal = discountedUnitPrice.multiply(
                    BigDecimal.valueOf(item.getQuantity())
            );

            BigDecimal itemDiscount = originalLineTotal.subtract(newLineTotal);
            if (itemDiscount.compareTo(BigDecimal.ZERO) < 0) {
                itemDiscount = BigDecimal.ZERO;
            }

            // Item-i yeniləyirik
            item.setUnitPrice(discountedUnitPrice);
            item.setLineTotal(newLineTotal);

            totalDiscountForOrder = totalDiscountForOrder.add(itemDiscount);
            newSubtotal = newSubtotal.add(newLineTotal);
        }

        // Heç bir məhsula endirim düşməyibsə → kupon uyğun deyil
        if (totalDiscountForOrder.compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("⚠️ Coupon {} not applicable for orderId={}", couponCode, orderId);
            throw BaseException.of(COUPON_NOT_APPLICABLE);
        }

        // OrderItems-ləri save edirik
        orderItemsRepo.saveAll(orderItems);

        // 5. Order modelini yeniləyirik
        order.setSubtotal(newSubtotal);                       // endirimdən sonrakı ümumi məbləğ
        order.setDiscountTotal(totalDiscountForOrder);        // bu kupondan gələn ümumi endirim
        BigDecimal shippingFee = order.getShippingFee() != null
                ? order.getShippingFee()
                : BigDecimal.ZERO;

        order.setGrandTotal(newSubtotal.add(shippingFee));    // final məbləğ = subtotal + shipping

        orderRepo.save(order);

        log.info("✅ Coupon {} applied successfully for orderId={}, totalDiscount={}",
                couponCode, orderId, totalDiscountForOrder);
    }






}
