package store.mybookstore.service.user.impl;

import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import store.mybookstore.dto.user.UserRegistrationRequestDto;
import store.mybookstore.dto.user.UserRegistrationResponseDto;
import store.mybookstore.exception.EntityNotFoundException;
import store.mybookstore.exception.RegistrationException;
import store.mybookstore.mapper.UserMapper;
import store.mybookstore.model.Role;
import store.mybookstore.model.User;
import store.mybookstore.repository.RoleRepository;
import store.mybookstore.repository.UserRepository;
import store.mybookstore.service.user.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserRegistrationResponseDto register(
            UserRegistrationRequestDto requestDto) throws RegistrationException {
        if (userRepository.findUserByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException(
                    "Unable to complete registration. "
                            + "Input email already exist: " + requestDto.getEmail());
        }
        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setShippingAddress(requestDto.getShippingAddress());
        Role userRole = roleRepository.findRoleByRoleName(Role.RoleName.ROLE_USER)
                .orElseThrow(() -> new RegistrationException("Can't find role for user"));
        Set<Role> defaultRole = new HashSet<>();
        defaultRole.add(userRole);
        user.setRoles(defaultRole);
        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Override
    public User getLoggedInUser() {
        String loggedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository
                .findUserByEmail(loggedUserEmail)
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find user by email,"
                                + " User may not logged in."));
    }
}
