package store.mybookstore.service.user;

import store.mybookstore.dto.user.UserRegistrationRequestDto;
import store.mybookstore.dto.user.UserRegistrationResponseDto;
import store.mybookstore.exception.RegistrationException;

public interface UserService {
    UserRegistrationResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException;
}
