package ru.yandex.practicum.service;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.NewProductInWarehouseRequest;
import ru.yandex.practicum.dto.ShoppingCartDto;

import java.util.Map;

public interface WarehouseService {

    void addNewProduct(@RequestBody NewProductInWarehouseRequest product);

    BookedProductsDto checkProductQuantityEnoughForShoppingCart(ShoppingCartDto shoppingCart);

    void addProductToWarehouse(AddProductToWarehouseRequest request);

    AddressDto getAddress();

    void assemblyProductForOrderFromShoppingCart(ShoppingCartDto cart);

    void shippedToDelivery(String deliveryId);

    void returnProducts(Map<String, Integer> products);
}
