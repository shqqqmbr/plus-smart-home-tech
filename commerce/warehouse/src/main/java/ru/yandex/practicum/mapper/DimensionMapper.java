package ru.yandex.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.DimensionDto;
import ru.yandex.practicum.model.Dimension;


@Component
public class DimensionMapper {

    public DimensionDto toDto(Dimension dimension) {
        return DimensionDto.builder()
                .width(dimension.getWidth())
                .height(dimension.getHeight())
                .depth(dimension.getDepth())
                .build();
    }

    public Dimension fromDto(DimensionDto dimensionDto) {
        return Dimension.builder()
                .width(dimensionDto.getWidth())
                .height(dimensionDto.getHeight())
                .depth(dimensionDto.getDepth())
                .build();
    }
}