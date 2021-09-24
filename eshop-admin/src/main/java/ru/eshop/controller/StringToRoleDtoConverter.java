package ru.eshop.controller;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.eshop.dto.RoleDto;

@Component
public class StringToRoleDtoConverter implements Converter<String, RoleDto> {
    @Override
    public RoleDto convert(String s) {
        String[] arr = s.split(";");
        return new RoleDto(Long.parseLong(arr[0]), arr[1]);
    }
}
