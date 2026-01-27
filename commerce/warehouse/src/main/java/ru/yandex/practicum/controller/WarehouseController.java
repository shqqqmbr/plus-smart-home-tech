package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.service.WarehouseService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouse")
@Validated
public class WarehouseController {
    private final WarehouseService warehouseService;

    @PutMapping()
    public void addNewProduct(@RequestBody NewProductInWarehouseRequest product) {
        warehouseService.addNewProduct(product);
    }

    @PostMapping("/check")
    public BookedProductsDto checkQuantity(@RequestBody ShoppingCartDto shoppingCart) {
        return warehouseService.checkProductQuantityEnoughForShoppingCart(shoppingCart);
    }

    @PostMapping("/add")
    public void acceptProduct(@RequestBody AddProductToWarehouseRequest product) {
        warehouseService.addProductToWarehouse(product);
    }

    @GetMapping("/address")
    public AddressDto getAddress() {
        return warehouseService.getAddress();
    }

    @PostMapping("/assembly")
    public BookedProductsDto assemblyProductForOrderFromShoppingCart(@RequestBody AssemblyProductsForOrderRequest request) {
        return warehouseService.assemblyProductForOrderFromShoppingCart(request);
    }

    @PostMapping("/shipped")
    public void shippedToDelivery(@RequestBody ShippedToDeliveryRequest request) {
        warehouseService.shippedToDelivery(request);
    }

    @PostMapping("/return")
    public void returnProducts(@RequestBody Map<String, Long> products) {
        warehouseService.returnProducts(products);
    }
}
