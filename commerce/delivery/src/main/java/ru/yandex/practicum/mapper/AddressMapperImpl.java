package ru.yandex.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.model.Address;

@Component
public class AddressMapperImpl implements AddressMapper {


    @Override
    public Address toEntity(AddressDto addressDto) {
        return Address.builder()
                .country(addressDto.getCountry())
                .city(addressDto.getCity())
                .street(addressDto.getStreet())
                .house(addressDto.getHouse())
                .flat(addressDto.getFlat())
                .build();
    }

    @Override
    public AddressDto toDto(Address address) {
        return AddressDto.builder()
                .country(address.getCountry())
                .city(address.getCity())
                .street(address.getStreet())
                .house(address.getHouse())
                .flat(address.getFlat())
                .build();
    }
}