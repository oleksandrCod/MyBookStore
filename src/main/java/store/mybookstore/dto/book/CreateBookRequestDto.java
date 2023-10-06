package store.mybookstore.dto.book;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @NotNull(message = "Field title can't be empty!")
    private String title;
    @NotNull(message = "Field author can't be empty!")
    private String author;
    @NotNull(message = "Field isbn can't be empty!")
    private String isbn;
    @NotNull(message = "Field price can't be empty")
    @Min(value = 0, message = "Price can't be less than zero.")
    @Digits(integer = 3, fraction = 3, message = "Invalid value"
            + " for field price. Max number is 3 digits.")
    private BigDecimal price;
    private String description;
    private String coverImage;
    private Set<Long> categoryId;
}
