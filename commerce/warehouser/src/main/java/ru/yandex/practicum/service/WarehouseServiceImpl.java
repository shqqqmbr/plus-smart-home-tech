package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.dto.NewProductInWarehouseRequest;

@Service
public class WarehouseServiceImpl implements WarehouseService{
    @Override
    public void addNewProduct(NewProductInWarehouseRequest product) {

    }

    @Override
    public boolean checkQuantity(String shoppingCartId, String orderId) {
        return false;
    }

    @Override
    public void acceptProduct() {

    }

    @Override
    public AddressDto getAddress() {
        return null;
    }
}
