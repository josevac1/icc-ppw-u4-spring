package ec.edu.ups.icc.fundamentos01.Category.models;

import ec.edu.ups.icc.fundamentos01.Category.Entity.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.Category.dto.CategoryResponseDTO;

public class Category {

    private Long id;
    private String name;
    private String description;

    public Category() {
    }

    public Category(String name, String description) {
        validate(name, description);
        this.name = name;
        this.description = description;
    }

    private void validate(String name, String description) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("El nombre no puede superar 100 caracteres");
        }
        if (description != null && description.length() > 500) {
            throw new IllegalArgumentException("La descripción no puede superar 500 caracteres");
        }
    }

    public static Category fromEntity(CategoryEntity entity) {
        Category model = new Category(entity.getName(), entity.getDescription());
        model.id = entity.getId();
        return model;
    }

    public CategoryEntity toEntity() {
        CategoryEntity entity = new CategoryEntity();
        if (id != null && id > 0) {
            entity.setId(id);
        }
        entity.setName(name);
        entity.setDescription(description);
        return entity;
    }

    public Category updateFromDto(CategoryResponseDTO dto) {
        String newName = dto.name != null ? dto.name : this.name;
        String newDescription = dto.description != null ? dto.description : this.description;
        validate(newName, newDescription);
        this.name = newName;
        this.description = newDescription;
        return this;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
