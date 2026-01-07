package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.dto.NewProductInWarehouseRequest;
import ru.yandex.practicum.service.WarehouseService;

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
    public boolean checkQuantity(String shoppingCartId, String orderId) {
        return warehouseService.checkQuantity(shoppingCartId, orderId);
    }

    @PostMapping("/add")
    public void acceptProduct(@RequestBody AddProductToWarehouseRequest product) {
        warehouseService.addProductToWarehouse(product);
    }

    @GetMapping("/address")
    public AddressDto getAddress() {
        return warehouseService.getAddress();
    }
}
