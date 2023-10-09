package store.mybookstore.dto.book;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Field price can't be empty")
    @Min(value = 0, message = "Price can't be less than zero.")
    @Digits(integer = 3, fraction = 3, message = "Invalid value"
            + " for field price. Max number is 3 digits.")
    private BigDecimal price;
    private String description;
    private String coverImage;
    private Set<Long> categoryId;
}
