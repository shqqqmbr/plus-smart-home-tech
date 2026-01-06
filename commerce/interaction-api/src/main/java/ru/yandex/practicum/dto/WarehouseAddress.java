package ru.yandex.practicum.dto;

import lombok.Data;

@Data
public class WarehouseAddress {
    private String country;
    private String city;
    private String street;
    private String house;
    private String flat;
}
