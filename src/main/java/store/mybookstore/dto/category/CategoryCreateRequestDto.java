package store.mybookstore.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryCreateRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
}
