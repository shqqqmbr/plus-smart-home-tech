package ru.yandex.practicum.service;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.dto.NewProductInWarehouseRequest;

public interface WarehouseService {

    void addNewProduct(@RequestBody NewProductInWarehouseRequest product);

    boolean checkQuantity(String shoppingCartId, String orderId);

    void addProductToWarehouse(AddProductToWarehouseRequest request);

    AddressDto getAddress();
}
