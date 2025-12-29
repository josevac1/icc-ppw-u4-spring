package ec.edu.ups.icc.fundamentos01.products.models;

import ec.edu.ups.icc.fundamentos01.products.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.PartialUpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.UpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;

public class Product {

    private int id;
    private String name;
    private String description;
    private Double price;
    private String createdAt;

    public Product(int id, String name, String description, Double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.createdAt = java.time.LocalDateTime.now().toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    // ==================== FACTORY METHODS ====================

    /**
     * Crea un Product desde un DTO de creación
     * @param dto DTO con datos del formulario
     * @return instancia de Product para lógica de negocio
     */
    public static Product fromDto(CreateProductDto dto) {
        Double price = dto.getPrice() != null ? dto.getPrice() : 0.0;
        return new Product(
            0,                // id = 0 porque aún no existe en BD
            dto.getName(),
            dto.getDescription(),
            price
        );
    }

    /**
     * Crea un Product desde una entidad persistente
     * @param entity Entidad recuperada de la BD
     * @return instancia de Product para lógica de negocio
     */
    public static Product fromEntity(ProductEntity entity) {
        return new Product(
            entity.getId().intValue(),
            entity.getName(),
            entity.getDescription(),
            entity.getPrice()
        );
    }

    // ==================== CONVERSION METHODS ====================

    /**
     * Convierte este Product a una entidad persistente
     * @return ProductEntity lista para guardar en BD
     */
    public ProductEntity toEntity() {
        ProductEntity entity = new ProductEntity();

        // Si ya tiene id, lo asignamos (para updates)
        if (this.id > 0) {
            entity.setId((long) this.id);
        }

        entity.setName(this.name);
        entity.setDescription(this.description);
        entity.setPrice(this.price != null ? this.price : 0.0);
        return entity;
    }

    /**
     * Convierte este Product a un DTO de respuesta
     * @return DTO sin información sensible
     */
    public ProductResponseDto toResponseDto() {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(this.id);
        dto.setName(this.name);
        dto.setDescription(this.description);
        dto.setPrice(this.price);
        return dto;
    }

    // ==================== MUTATION METHODS ====================

    public Product update(UpdateProductDto dto) {
        this.name = dto.name;
        this.description = dto.description;
        this.price = dto.price;
        return this;
    }

    public Product partialUpdate(PartialUpdateProductDto dto) {
        if (dto.name != null) this.name = dto.name;
        if (dto.description != null) this.description = dto.description;
        if (dto.price != null) this.price = dto.price;
        return this;
    }
}
