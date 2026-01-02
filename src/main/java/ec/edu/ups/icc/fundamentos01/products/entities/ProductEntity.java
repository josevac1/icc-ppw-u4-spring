package ec.edu.ups.icc.fundamentos01.products.entities;

import jakarta.persistence.*;
import ec.edu.ups.icc.fundamentos01.core.entities.BaseModel;

@Entity
@Table(name = "products")
public class ProductEntity extends BaseModel {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer stock = 0;

    // ==================== CONSTRUCTORS ====================
    public ProductEntity() {
    }

    public ProductEntity(String name, String description, Double price, Integer stock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    // ==================== GETTERS AND SETTERS ====================
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
}
