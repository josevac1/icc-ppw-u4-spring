package ec.edu.ups.icc.fundamentos01.Category.mappers;

import ec.edu.ups.icc.fundamentos01.Category.Entity.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.Category.dto.CategoryResponseDTO;
import ec.edu.ups.icc.fundamentos01.Category.models.Category;

public class CategoryMapper {

    public static Category toModel(CategoryEntity entity) {
        return Category.fromEntity(entity);
    }

    public static CategoryEntity toEntity(CategoryResponseDTO dto) {
        Category category = new Category(dto.name, dto.description);
        category.setId(dto.id);
        return category.toEntity();
    }

    public static CategoryResponseDTO toResponse(CategoryEntity entity) {
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.id = entity.getId();
        dto.name = entity.getName();
        dto.description = entity.getDescription();
        return dto;
    }

    public static CategoryResponseDTO toResponse(Category model) {
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.id = model.getId();
        dto.name = model.getName();
        dto.description = model.getDescription();
        return dto;
    }
}
