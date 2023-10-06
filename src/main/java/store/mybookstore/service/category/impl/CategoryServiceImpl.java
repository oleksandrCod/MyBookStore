package store.mybookstore.service.category.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import store.mybookstore.dto.book.BookDtoWithoutCategoryIds;
import store.mybookstore.dto.category.CategoryCreateRequestDto;
import store.mybookstore.dto.category.CategoryDto;
import store.mybookstore.exception.EntityNotFoundException;
import store.mybookstore.mapper.BookMapper;
import store.mybookstore.mapper.CategoryMapper;
import store.mybookstore.model.Category;
import store.mybookstore.repository.BookRepository;
import store.mybookstore.repository.CategoryRepository;
import store.mybookstore.service.category.CategoryService;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public CategoryDto save(CategoryCreateRequestDto requestDto) {
        Category model = categoryMapper.toModel(requestDto);
        return categoryMapper.toDto(categoryRepository.save(model));
    }

    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find category with input id:" + id));
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto update(Long id, CategoryCreateRequestDto requestDto) {
        return categoryRepository.findById(id).map(category -> {
            category.setName(requestDto.getName());
            category.setDescription(requestDto.getDescription());
            return categoryMapper.toDto(categoryRepository.save(category));
        }).orElseThrow(
                () -> new EntityNotFoundException("Can't update category with input id:" + id));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Pageable pageable, Long id) {
        return bookRepository.findBooksByCategoriesId(id)
                .stream()
                .map(bookMapper::toDtoWithoutCategoryIds)
                .collect(Collectors.toList());
    }
}
