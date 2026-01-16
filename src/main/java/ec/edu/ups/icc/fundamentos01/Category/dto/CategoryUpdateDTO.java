package ec.edu.ups.icc.fundamentos01.Category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryUpdateDTO {
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    public String name;

    @Size(max = 500, message = "La descripción no puede superar 500 caracteres")
    public String description;

    public CategoryUpdateDTO() {}

    public CategoryUpdateDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
