package ru.yandex.practicum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;
import ru.yandex.practicum.constant.ProductCategory;
import ru.yandex.practicum.constant.ProductState;
import ru.yandex.practicum.constant.QuantityState;

import java.util.UUID;

@Entity
@Table(name = "products")
@Data
@Builder
@EqualsAndHashCode(of = "productId")
@RequiredArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @Column(name = "product_id", nullable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "UUID")
    UUID productId;

    @Column(name = "name", nullable = false)
    String productName;

    @Column(name = "description", nullable = false)
    String description;

    @Column(name = "image_src", nullable = false)
    String imageSrc;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "quantity_state")
    QuantityState quantityState;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "product_state")
    ProductState productState;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    ProductCategory productCategory;

    @Min(1)
    @Column(name = "price", nullable = false)
    double price;

}


