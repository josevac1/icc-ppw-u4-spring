package ec.edu.ups.icc.fundamentos01.Category.dto;

import jakarta.validation.constraints.Size;

public class CategoryPatchDTO {
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    public String name; // opcional

    @Size(max = 500, message = "La descripción no puede superar 500 caracteres")
    public String description; // opcional

    public CategoryPatchDTO() {}

    public CategoryPatchDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
