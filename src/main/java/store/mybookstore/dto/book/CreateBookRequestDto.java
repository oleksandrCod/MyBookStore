package store.mybookstore.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @NotBlank(message = "Field title can't be empty!")
    private String title;
    @NotBlank(message = "Field author can't be empty!")
    private String author;
    @NotBlank(message = "Field isbn can't be empty!")
    private String isbn;
    @NotNull(message = "Field price can't be empty")
    @Positive
    private BigDecimal price;
    private String description;
    private String coverImage;
    private Set<Long> categoryId;
}
