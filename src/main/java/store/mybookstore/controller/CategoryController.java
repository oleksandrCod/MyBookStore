package store.mybookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import store.mybookstore.dto.book.BookDtoWithoutCategoryIds;
import store.mybookstore.dto.category.CategoryCreateRequestDto;
import store.mybookstore.dto.category.CategoryDto;
import store.mybookstore.service.category.CategoryService;

@RequiredArgsConstructor
@RestController
@Tag(name = "Categories management", description = "Categories endpoints")
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    @Operation(summary = "Create new Category", description = "Allows to user with "
            + "Admin role create new category.")
    public CategoryDto createCategory(@RequestBody CategoryCreateRequestDto requestDto) {
        return categoryService.save(requestDto);
    }

    @GetMapping
    @Operation(summary = "Get list of Categories", description = "Allows the user to "
            + "get list of all categories.")
    public List<CategoryDto> getAll(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by id", description = "Allows the user to"
            + " get category by special id.")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update Category", description = "Update category by special id")
    public CategoryDto updateCategoryById(
            @PathVariable Long id, @RequestBody CategoryCreateRequestDto requestDto) {
        return categoryService.update(id, requestDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete Category", description = "Allows the user with "
            + "role Admin to delete category by special id.")
    void deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @GetMapping("/{id}/books")
    @Operation(summary = "Get book by category id", description = "Allows the user to"
            + " get list of books with special category.")
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(
            @PathVariable Long id, Pageable pageable) {
        return categoryService.getBooksByCategoryId(pageable, id);
    }
}

