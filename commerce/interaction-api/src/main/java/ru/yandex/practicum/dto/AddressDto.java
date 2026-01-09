package ru.yandex.practicum.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressDto {
    String country;
    String city;
    String street;
    String house;
    String flat;
}
