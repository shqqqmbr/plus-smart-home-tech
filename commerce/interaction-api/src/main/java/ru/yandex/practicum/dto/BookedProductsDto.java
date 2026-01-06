package ru.yandex.practicum.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookedProductsDto {
    private double deliveryWeight;

    private double deliveryVolume;
    @NotNull
    private boolean fragile;
}
