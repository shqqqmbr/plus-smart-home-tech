package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.mapper.DimensionMapper;
import ru.yandex.practicum.model.WarehouseProduct;
import ru.yandex.practicum.repository.WarehouseRepository;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final DimensionMapper  dimensionMapper;

    @Override
    public void addNewProduct(NewProductInWarehouseRequest productRequest) {
        UUID productId = UUID.fromString(productRequest.productId());

        if (warehouseRepository.existsByProductId(productId)) {
            throw new SpecifiedProductAlreadyInWarehouseException(
                    "Товар с ID " + productId + " уже зарегистрирован на складе"
            );
        }

        WarehouseProduct warehouseProduct = WarehouseProduct.builder()
                .productId(productId)
                .fragile(productRequest.fragile())
                .dimension(dimensionMapper.fromDto(productRequest.dimension()))
                .weight(productRequest.weight())
                .quantity(0)
                .build();

        warehouseRepository.save(warehouseProduct);
    }

    @Override
    public boolean checkQuantity(String shoppingCartId, String orderId) {
        return false;
    }

    public BookedProductsDto checkProductQuantityEnoughForShoppingCart(ShoppingCartDto shoppingCart) {
        Map<String, Integer> products = shoppingCart.getProducts();

        double totalWeight = 0.0;
        double totalVolume = 0.0;
        boolean hasFragile = false;

        for (Map.Entry<String, Integer> entry : products.entrySet()) {
            UUID productId = UUID.fromString(entry.getKey());
            int requestedQuantity = entry.getValue();

            WarehouseProduct warehouseProduct = warehouseRepository.findByProductId(productId)
                    .orElseThrow(() -> new NoSpecifiedProductInWarehouseException(
                            "Товар с ID " + productId + " не найден на складе"
                    ));

            if (warehouseProduct.getQuantity() < requestedQuantity) {
                throw new ProductInShoppingCartLowQuantityInWarehouse(
                        "Недостаточно товара " + productId + " на складе. " +
                                "Запрошено: " + requestedQuantity + ", доступно: " + warehouseProduct.getQuantity()
                );
            }

            totalWeight += warehouseProduct.getWeight() * requestedQuantity;
            totalVolume += calculateVolume(dimensionMapper.toDto(warehouseProduct.getDimension())) * requestedQuantity;

            if (warehouseProduct.isFragile()) {
                hasFragile = true;
            }
        }

        return BookedProductsDto.builder()
                .deliveryWeight(totalWeight)
                .deliveryVolume(totalVolume)
                .fragile(hasFragile)
                .build();
    }

    @Override
    public void addProductToWarehouse(AddProductToWarehouseRequest request) {
        UUID productId = UUID.fromString(request.productId());
        int quantity = request.quantity();

        WarehouseProduct warehouseProduct = warehouseRepository.findByProductId(productId)
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException(
                        "Товар с ID " + productId + " не найден на складе"
                ));

        warehouseProduct.setQuantity(warehouseProduct.getQuantity() + quantity);
        warehouseRepository.save(warehouseProduct);
    }

    @Override
    public AddressDto getAddress() {
        WarehouseAddress address = new WarehouseAddress();
        return AddressDto.builder()
                .country(address.getCountry())
                .city(address.getCity())
                .street(address.getStreet())
                .house(address.getHouse())
                .flat(address.getFlat())
                .build();
    }

    private double calculateVolume(DimensionDto dimension) {
        return dimension.getWidth() * dimension.getHeight() * dimension.getDepth();
    }
}