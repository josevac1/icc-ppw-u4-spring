package ec.edu.ups.icc.fundamentos01.products.models;

import java.time.LocalDateTime;
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
    private Integer stock;
    private LocalDateTime createdAt;

    public Product(int id, String name, String description, Double price, Integer stock) {
        // Validaciones de reglas de negocio
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es inválido");
        }

        if (price == null || price < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }

        if (stock == null || stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }

        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.createdAt = LocalDateTime.now();
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

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // ==================== FACTORY METHODS ====================

    /**
     * Crea un Product desde un DTO de creación
     * @param dto DTO con datos del formulario
     * @return instancia de Product para lógica de negocio
     */
    public static Product fromDto(CreateProductDto dto) {
        return new Product(
            0,  // id = 0 porque aún no existe en BD
            dto.getName(),
            dto.getDescription(),
            dto.getPrice() != null ? dto.getPrice() : 0.0,
            dto.getStock() != null ? dto.getStock() : 0
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
            entity.getPrice(),
            entity.getStock()
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
        entity.setStock(this.stock != null ? this.stock : 0);
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
        dto.setStock(this.stock);
        dto.setCreatedAt(this.createdAt.toString());
        return dto;
    }

    // ==================== MUTATION METHODS ====================

    public Product update(UpdateProductDto dto) {
        // Validar reglas de negocio antes de actualizar
        if (dto.getPrice() != null && dto.getPrice() < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (dto.getStock() != null && dto.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }

        this.name = dto.name;
        this.description = dto.description;
        this.price = dto.price;
        this.stock = dto.stock;
        return this;
    }

    public Product partialUpdate(PartialUpdateProductDto dto) {
        // Validar reglas de negocio antes de actualizar
        if (dto.getPrice() != null && dto.getPrice() < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (dto.getStock() != null && dto.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }

        if (dto.name != null) this.name = dto.name;
        if (dto.description != null) this.description = dto.description;
        if (dto.price != null) this.price = dto.price;
        if (dto.stock != null) this.stock = dto.stock;
        return this;
    }
}
