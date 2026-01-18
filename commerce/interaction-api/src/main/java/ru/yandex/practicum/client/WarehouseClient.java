package ru.yandex.practicum.client;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.*;

@FeignClient(name = "warehouse")
@RequestMapping("/api/v1/warehouse")
public interface WarehouseClient {

    @PutMapping
    void addNewProduct(@RequestBody @Valid NewProductInWarehouseRequest newProductInWarehouseRequestDto);

    @PostMapping("/check")
    BookedProductsDto checkQuantity(@RequestBody @Valid ShoppingCartDto shoppingCartDto);

    @PostMapping("/add")
    void acceptProduct(@RequestBody @Valid AddProductToWarehouseRequest addProductToWarehouseRequestDto);

    @GetMapping("/address")
    AddressDto getAddress();

    @PostMapping("/shipped")
    void shippedToDelivery(@RequestBody String deliveryId);

    @PostMapping("/return")
    void returnProducts(@RequestBody java.util.Map<String, Integer> products);

    @PostMapping("/assembly")
    void assemblyProductForOrderFromShoppingCart(@RequestBody ShoppingCartDto cart);
}
