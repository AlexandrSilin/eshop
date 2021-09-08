package ru.eshop.controller;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.eshop.dto.CategoryDto;

@Component
public class StringToCategoryDtoConverter implements Converter<String, CategoryDto> {

    @Override
    public CategoryDto convert(String s) {
        String[] arr = s.split(";");
        return new CategoryDto(Long.parseLong(arr[0]), arr[1]);
    }
}
