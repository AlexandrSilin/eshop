package ru.eshop.service;

import org.springframework.data.domain.Page;
import ru.eshop.controller.UserListParams;
import ru.eshop.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDto> findAll();

    Page<UserDto> findWithFilter(UserListParams params);

    Optional<UserDto> findById(Long Id);

    void save(UserDto user);

    void deleteById(Long id);
}
