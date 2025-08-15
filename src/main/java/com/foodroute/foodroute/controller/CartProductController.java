package com.foodroute.foodroute.controller;

import com.foodroute.foodroute.dto.CartProductDto;
import com.foodroute.foodroute.model.CartProduct;
import com.foodroute.foodroute.repository.CartProductRepository;
import com.foodroute.foodroute.service.CartProductService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart-product")
public class CartProductController {

    private final CartProductService cartProductService;

    public CartProductController(CartProductService cartProductService) {
        this.cartProductService = cartProductService;
    }

    @GetMapping
    public List<CartProduct> findAll(@RequestParam Long cartId) {
        return cartProductService.findCartProductsByCartId(cartId);
    }

    @PostMapping
    public CartProduct addCartProduct(@RequestBody CartProductDto cartProductDto) {
        return cartProductService.addCartProduct(cartProductDto);
    }

    @PutMapping("/{id}")
    public CartProduct editCartProduct(@RequestBody CartProductDto cartProductDto, @PathVariable Long id) {
        return cartProductService.editCartProduct(cartProductDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteCartProduct(@PathVariable Long id) {
        cartProductService.removeCartProduct(id);
    }

    @Transactional
    @DeleteMapping
    public void deleteCartProductsByCartId(@RequestParam Long cartId) {
        cartProductService.removeCartProductByCartId(cartId);
    }
}
