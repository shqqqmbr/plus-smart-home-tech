package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.dto.NewProductInWarehouseRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouse")
public class WarehouseController {
    @PutMapping()
    public void addNewProduct(@RequestBody NewProductInWarehouseRequest product){

    }

    @PostMapping("/check")
    public boolean checkQuantity(String shoppingCartId, String orderId){
        return true;
    }

    @PostMapping("/add")
    public void acceptProduct(){

    }

    @GetMapping("/address")
    public AddressDto getAddress(){

    }
}
