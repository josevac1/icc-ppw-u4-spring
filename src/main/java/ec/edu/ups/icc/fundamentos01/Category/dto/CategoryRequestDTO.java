package ec.edu.ups.icc.fundamentos01.Category.dto;

public class CategoryRequestDTO {
    public String name;
    public String description;

    public CategoryRequestDTO() {
    }

    public CategoryRequestDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
