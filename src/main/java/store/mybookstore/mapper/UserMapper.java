package store.mybookstore.mapper;

import org.mapstruct.Mapper;
import store.mybookstore.config.MapperConfig;
import store.mybookstore.dto.user.UserRegistrationResponseDto;
import store.mybookstore.model.User;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserRegistrationResponseDto toResponseDto(User user);
}
