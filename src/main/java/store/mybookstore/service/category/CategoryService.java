package store.mybookstore.service.category;

import java.util.List;
import org.springframework.data.domain.Pageable;
import store.mybookstore.dto.book.BookDtoWithoutCategoryIds;
import store.mybookstore.dto.category.CategoryCreateRequestDto;
import store.mybookstore.dto.category.CategoryDto;

public interface CategoryService {
    CategoryDto save(CategoryCreateRequestDto requestDto);

    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto update(Long id, CategoryCreateRequestDto requestDto);

    void deleteById(Long id);

    List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Pageable pageable, Long id);

}
