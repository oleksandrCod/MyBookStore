package store.mybookstore.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CategoryCreateRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
}
