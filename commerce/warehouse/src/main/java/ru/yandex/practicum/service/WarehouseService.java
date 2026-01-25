package ru.yandex.practicum.service;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.*;

import java.util.Map;

public interface WarehouseService {

    void addNewProduct(@RequestBody NewProductInWarehouseRequest product);

    BookedProductsDto checkProductQuantityEnoughForShoppingCart(ShoppingCartDto shoppingCart);

    void addProductToWarehouse(AddProductToWarehouseRequest request);

    AddressDto getAddress();

    BookedProductsDto assemblyProductForOrderFromShoppingCart(AssemblyProductsForOrderRequest request);

    void shippedToDelivery(ShippedToDeliveryRequest request);

    void returnProducts(Map<String, Long> products);
}
