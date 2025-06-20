package test.project.technical.task.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ProductRequestDto(
        @NotBlank
        String name,
        @NotBlank
        String description,
        @NotNull
        @DecimalMin("0.0")
        BigDecimal price,
        @NotBlank
        String category,
        @Min(0)
        Integer stock) {
}
