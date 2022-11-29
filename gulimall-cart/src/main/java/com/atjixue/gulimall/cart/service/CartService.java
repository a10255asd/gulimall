package com.atjixue.gulimall.cart.service;

import com.atjixue.gulimall.cart.vo.Cart;
import com.atjixue.gulimall.cart.vo.CartItem;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @Author LiuJixue
 * @Date 2022/9/2 16:49
 * @PackageName:com.atjixue.gulimall.cart.service
 * @ClassName: CartService
 * @Description: TODO
 * @Version 1.0
 */
public interface CartService {
    CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    CartItem getCartItem(Long skuId);

    Cart getCart() throws ExecutionException, InterruptedException;

    void clearCart(String cartKey);

    void checkItem(Long skuId, Integer check);

    void changeItemCount(Long skuId, Integer num);

    void deleteItem(Long skuId);

    List<CartItem> getUserCartItems();
}
