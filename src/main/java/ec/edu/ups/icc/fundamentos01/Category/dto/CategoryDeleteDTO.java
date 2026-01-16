package ec.edu.ups.icc.fundamentos01.Category.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CategoryDeleteDTO {
    @NotNull(message = "El ID de la categoría es obligatorio")
    public Long id;

    @Size(max = 200, message = "La razón no puede superar 200 caracteres")
    public String reason; // opcional, para auditoría

    public CategoryDeleteDTO() {}

    public CategoryDeleteDTO(Long id, String reason) {
        this.id = id;
        this.reason = reason;
    }
}
