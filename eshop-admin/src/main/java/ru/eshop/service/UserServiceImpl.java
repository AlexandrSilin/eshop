package ru.eshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.eshop.controller.UserListParams;
import ru.eshop.database.persist.RoleRepository;
import ru.eshop.database.persist.UserRepository;
import ru.eshop.database.persist.UserSpecifications;
import ru.eshop.database.persist.model.User;
import ru.eshop.dto.RoleDto;
import ru.eshop.dto.UserDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private static Set<RoleDto> getUserRoles(User user) {
        return user.getRoles().stream().map(role -> new RoleDto(role.getId(), role.getName())).collect(Collectors.toSet());
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(user -> new UserDto(user.getId(), user.getUsername(), user.getAge())).collect(Collectors.toList());
    }

    @Override
    public Page<UserDto> findWithFilter(UserListParams params) {
        Specification<User> specification = Specification.where(null);
        if (params.getUsernamePrefix() != null && !params.getUsernamePrefix().isBlank()) {
            specification.and(UserSpecifications.usernamePrefix(params.getUsernamePrefix()));
        }
        if (params.getMaxAge() != null) {
            specification.and(UserSpecifications.maxAge(params.getMaxAge()));
        }
        if (params.getMinAge() != null) {
            specification.and(UserSpecifications.minAge(params.getMinAge()));
        }
        return userRepository.findAll(specification,
                        PageRequest.of(
                                Optional.ofNullable(params.getPage()).orElse(1) - 1,
                                Optional.ofNullable(params.getSize()).orElse(5),
                                Sort.by(Optional.ofNullable(params.getSortField()).filter(c -> !c.isBlank()).orElse("id"))))
                .map(user -> new UserDto(user.getId(), user.getUsername(), user.getAge()));
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        return userRepository.findById(id).map(user -> new UserDto(user.getId(), user.getUsername(), user.getAge(),
                getUserRoles(user)));
    }

    @Override
    public void save(UserDto userDto) {
        userRepository.save(new User(
                userDto.getId(),
                userDto.getUsername(),
                passwordEncoder.encode(userDto.getPassword()),
                userDto.getAge(),
                userDto.getRoles().stream()
                        .map(roleDto -> roleRepository.getOne(roleDto.getId()))
                        .collect(Collectors.toSet())));
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
