package com.foodroute.foodroute.service;

import com.foodroute.foodroute.dto.CartProductDto;
import com.foodroute.foodroute.model.Cart;
import com.foodroute.foodroute.model.CartProduct;
import com.foodroute.foodroute.model.Product;
import com.foodroute.foodroute.repository.CartProductRepository;
import com.foodroute.foodroute.repository.CartRepository;
import com.foodroute.foodroute.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartProductService {

    private final CartProductRepository cartProductRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public CartProductService(CartProductRepository cartProductRepository, ProductRepository productRepository, CartRepository cartRepository) {
        this.cartProductRepository = cartProductRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    public List<CartProduct> findCartProductsByCartId(Long id) {
        return cartProductRepository.findByCartId(id);
    }

    @Transactional
    public CartProduct addCartProduct(CartProductDto cartProductDto) {
        Cart cart = cartRepository.findById(cartProductDto.getCartId()).orElseThrow(() -> new RuntimeException("Cart not found"));
        Product product = productRepository.findById(cartProductDto.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));
        CartProduct cartProduct = new CartProduct(cartProductDto.getQuantity(), cart, product);
        return cartProductRepository.save(cartProduct);
    }

    @Transactional
    public CartProduct editCartProduct(CartProductDto cartProductDto, Long id) {
        CartProduct cartProduct = cartProductRepository.findById(id).orElseThrow(() -> new RuntimeException("Cart Product not found"));
        Cart cart = cartRepository.findById(cartProductDto.getCartId()).orElseThrow(() -> new RuntimeException("Cart not found"));
        Product product = productRepository.findById(cartProductDto.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));
        cartProduct.setCart(cart);
        cartProduct.setProduct(product);
        cartProduct.setQuantity(cartProductDto.getQuantity());
        return cartProductRepository.save(cartProduct);
    }

    @Transactional
    public void removeCartProduct(Long id) {
        cartProductRepository.deleteById(id);
    }

    public void removeCartProductByCartId(Long cartId) {
        cartProductRepository.deleteAllByCartId(cartId);
    }

}
