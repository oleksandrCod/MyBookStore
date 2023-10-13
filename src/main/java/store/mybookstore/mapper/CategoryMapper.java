package store.mybookstore.mapper;

import org.mapstruct.Mapper;
import store.mybookstore.config.MapperConfig;
import store.mybookstore.dto.category.CategoryCreateRequestDto;
import store.mybookstore.dto.category.CategoryDto;
import store.mybookstore.model.Category;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    Category toModel(CategoryCreateRequestDto requestDto);

    CategoryDto toDto(Category category);
}
