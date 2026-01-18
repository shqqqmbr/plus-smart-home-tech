package ru.yandex.practicum.mapper;

import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.model.Address;

public interface AddressMapper {

    Address toEntity(AddressDto addressDto);

    AddressDto toDto(Address address);
}
