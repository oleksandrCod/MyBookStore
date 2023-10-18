package store.mybookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
import store.mybookstore.dto.book.BookDto;
import store.mybookstore.dto.book.CreateBookRequestDto;
import store.mybookstore.dto.book.records.BookSearchParameters;
import store.mybookstore.service.book.BookService;

@Tag(name = "Books management", description = "Endpoints for managing books")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Get all books.", description = "Return list of all present books")
    public List<BookDto> getAll(Pageable pageable) {
        return bookService.getAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID", description = "Return present book by ID")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new book", description = "Create new book")
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.createBook(requestDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete book by ID", description = "Soft delete for book with input ID")
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update book", description = "Update present book by ID")
    public BookDto updateBookById(@PathVariable Long id,
                                  @RequestBody CreateBookRequestDto requestDto) {
        return bookService.updateById(id, requestDto);
    }

    @GetMapping("/search")
    @Operation(summary = "Search books", description = "Return list of books"
            + " with special input parameters")
    public List<BookDto> search(BookSearchParameters bookSearchParameters) {
        return bookService.search(bookSearchParameters);
    }
}
