package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("warehouse.address")
@Data
public class WarehouseAddress {
    @NotNull
    private String country;
    @NotNull
    private String city;
    @NotNull
    private String street;
    @NotNull
    private String house;
    @NotNull
    private String flat;
}
