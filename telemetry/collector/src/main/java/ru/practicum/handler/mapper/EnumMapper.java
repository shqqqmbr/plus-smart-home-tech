package ru.practicum.handler.mapper;

public class EnumMapper {

    public static <T extends Enum<T>> T map(Enum<?> source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }

        try {
            return Enum.valueOf(targetClass, source.name());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Unsupported mapping from " + source.getClass().getSimpleName() +
                            " to " + targetClass.getSimpleName() +
                            ". No matching enum value for: " + source.name()
            );
        }
    }
}