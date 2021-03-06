package ru.eshop.controller;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.eshop.dto.BrandDto;

@Component
public class StringToBrandDtoConverter implements Converter<String, BrandDto> {
    @Override
    public BrandDto convert(String s) {
        String[] arr = s.split(";");
        return new BrandDto(Long.parseLong(arr[0]), arr[1]);
    }
}
