package ec.edu.ups.icc.fundamentos01.products.models;

import ec.edu.ups.icc.fundamentos01.products.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.PartialUpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.UpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;

public class Product {

    private Long id;
    private String name;
    private Double price;
    private int stock;
    private String description;

    // Constructores
    public Product() {
    }

    public Product(String name, Double price, String description) {
        this.validateBusinessRules(name, price, description);
        this.name = name;
        this.price = price;
        this.description = description;
    }

    private void validateBusinessRules(String name, Double price, String description) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        if (price == null || price <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
        if (description != null && description.length() > 500) {
            throw new IllegalArgumentException("La descripción no puede superar 500 caracteres");
        }
    }

    // ==================== FACTORY METHODS ====================

    /**
     * Crea un Product desde un DTO de creación
     */
    public static Product fromDto(CreateProductDto dto) {
        Product product = new Product(dto.name, dto.price, dto.description);
        product.stock = dto.stock != null ? dto.stock : 0;
        return product;
    }

    /**
     * Crea un Product desde una entidad persistente
     */
    public static Product fromEntity(ProductEntity entity) {
        Product product = new Product(
                entity.getName(),
                entity.getPrice(),
                entity.getDescription());
        product.id = entity.getId();
        return product;
    }

    // ==================== CONVERSION METHODS ====================

    /**
     * Convierte este Product a una entidad persistente
     * Solo asigna el owner, las categorías se asignan por separado
     */
    public ProductEntity toEntity(UserEntity owner) {
        ProductEntity entity = new ProductEntity();

        if (this.id != null && this.id > 0) {
            entity.setId(this.id);
        }

        entity.setName(this.name);
        entity.setPrice(this.price);
        entity.setDescription(this.description);
        entity.setStock(this.stock);

        // Asignar relación 1:N con usuario
        entity.setOwner(owner);

        return entity;
    }

    /**
     * Convierte este Product a una entidad persistente SIN relaciones
     * Útil para operaciones donde owner/categorías no aplican o se asignan luego.
     */
    public ProductEntity toEntity() {
        ProductEntity entity = new ProductEntity();
        if (this.id != null && this.id > 0) {
            entity.setId(this.id);
        }
        entity.setName(this.name);
        entity.setPrice(this.price);
        entity.setDescription(this.description);
        entity.setStock(this.stock);
        return entity;
    }

    /**
     * Actualiza los campos modificables de este Product
     */
    public Product update(UpdateProductDto dto) {
        this.validateBusinessRules(dto.name, dto.price, dto.description);
        this.name = dto.name;
        this.price = dto.price;
        this.description = dto.description;
        return this;
    }

    /**
     * Actualización parcial: solo aplica campos no nulos del DTO.
     */
    public Product partialUpdate(PartialUpdateProductDto dto) {
        String newName = dto.name != null ? dto.name : this.name;
        Double newPrice = dto.price != null ? dto.price : this.price;
        String newDescription = dto.description != null ? dto.description : this.description;
        this.validateBusinessRules(newName, newPrice, newDescription);
        this.name = newName;
        this.price = newPrice;
        this.description = newDescription;
        return this;
    }

    public ProductResponseDto toResponseDto() {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(this.id);
        dto.setName(this.name);
        dto.setDescription(this.description != null ? this.description : "");
        dto.setPrice(this.price);
        dto.setStock(this.stock);
        // Nota: el mapeo de owner, categorías y auditoría
        // se realiza desde ProductServiceImpl usando ProductEntity.
        return dto;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

}
