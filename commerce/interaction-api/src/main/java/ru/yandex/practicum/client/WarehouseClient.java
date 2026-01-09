package ru.yandex.practicum.client;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.*;

@FeignClient(name = "warehouse")
public interface WarehouseClient {
    void addNewProductToWarehouse(@RequestBody @Valid NewProductInWarehouseRequest newProductInWarehouseRequestDto);

    BookedProductsDto checkProductQuantityInWarehouse(@RequestBody @Valid ShoppingCartDto shoppingCartDto);

    void updateProductToWarehouse(@RequestBody @Valid AddProductToWarehouseRequest addProductToWarehouseRequestDto);

    AddressDto getWarehouseAddress();
}
