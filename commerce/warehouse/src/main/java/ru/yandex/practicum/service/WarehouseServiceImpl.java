package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.mapper.DimensionMapper;
import ru.yandex.practicum.mapper.ReservedProductMapper;
import ru.yandex.practicum.model.ReservedProduct;
import ru.yandex.practicum.model.WarehouseProduct;
import ru.yandex.practicum.repository.ReservedProductRepository;
import ru.yandex.practicum.repository.WarehouseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final ReservedProductRepository reservedProductRepository;
    private final DimensionMapper dimensionMapper;
    private final WarehouseAddress address;
    private final ReservedProductMapper reservedProductMapper;

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
    public BookedProductsDto checkProductQuantityEnoughForShoppingCart(ShoppingCartDto shoppingCart) {
        Map<String, Long> products = shoppingCart.getProducts();

        double totalWeight = 0.0;
        double totalVolume = 0.0;
        boolean hasFragile = false;

        for (Map.Entry<String, Long> entry : products.entrySet()) {
            UUID productId = UUID.fromString(entry.getKey());
            long requestedQuantity = entry.getValue();

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
        return AddressDto.builder()
                .country(address.getCountry())
                .city(address.getCity())
                .street(address.getStreet())
                .house(address.getHouse())
                .flat(address.getFlat())
                .build();
    }

    @Override
    public BookedProductsDto assemblyProductForOrderFromShoppingCart(AssemblyProductsForOrderRequest request) {
        Map<UUID, Long> productsForAssembly = request.products().entrySet().stream()
                .collect(Collectors.toMap(e -> UUID.fromString(e.getKey()), Map.Entry::getValue));

        List<WarehouseProduct> warehouseProductList = warehouseRepository.findAllByProductIdIn(
                productsForAssembly.keySet().stream()
                        .toList()
        );

        Map<UUID, WarehouseProduct> warehouseProductMap = warehouseProductList.stream()
                .collect(Collectors.toMap(WarehouseProduct::getProductId, product -> product));

        List<ReservedProduct> reservedProductsList = new ArrayList<>();
        for (Map.Entry<UUID, Long> entry : productsForAssembly.entrySet()) {
            ReservedProduct reservedProduct = ReservedProduct.builder()
                    .orderId(UUID.fromString(request.orderId()))
                    .productId(entry.getKey())
                    .reservedQuantity(entry.getValue())
                    .build();
            reservedProductsList.add(reservedProduct);
        }
        List<ReservedProduct> saveOrderBookingList = reservedProductRepository.saveAll(reservedProductsList);
        return reservedProductMapper.toBookedProductsDto(saveOrderBookingList, warehouseProductMap);
    }

    @Override
    public void shippedToDelivery(ShippedToDeliveryRequest request) {
        List<ReservedProduct> reservedProducts = reservedProductRepository.findByOrderIdAndDeliveryId(
                UUID.fromString(request.orderId()),
                UUID.fromString(request.deliveryId())
        );
        for (ReservedProduct reservedProduct : reservedProducts) {
            reservedProduct.setDeliveryId(UUID.fromString(request.deliveryId()));
        }
        reservedProductRepository.saveAll(reservedProducts);
    }

    @Override
    public void returnProducts(Map<String, Long> products) {
        for (Map.Entry<String, Long> entry : products.entrySet()) {
            UUID productId = UUID.fromString(entry.getKey());
            Long quantity = entry.getValue();

            WarehouseProduct warehouseProduct = warehouseRepository.findByProductId(productId)
                    .orElseThrow(() -> new NoSpecifiedProductInWarehouseException(
                            "Товар с ID " + productId + " не найден на складе"
                    ));

            warehouseProduct.setQuantity(warehouseProduct.getQuantity() + quantity);
            warehouseRepository.save(warehouseProduct);
        }
    }

    private double calculateVolume(DimensionDto dimension) {
        return dimension.getWidth() * dimension.getHeight() * dimension.getDepth();
    }
}