package ec.edu.ups.icc.fundamentos01.products.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

import ec.edu.ups.icc.fundamentos01.Category.Entity.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.core.entities.BaseModel;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;

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

    /// atributos relacionados con usuarios donde un 1 usuarios puedes tener
    /// vario productos

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity owner;

    // ============== NUEVA RELACIÓN N:N ==============

    /**
     * Relación Many-to-Many con Category
     * Un producto puede tener múltiples categorías
     * Una categoría puede estar en múltiples productos
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_categories", // Tabla intermedia
            joinColumns = @JoinColumn(name = "product_id"), // FK hacia products
            inverseJoinColumns = @JoinColumn(name = "category_id") // FK hacia categories
    )
    private Set<CategoryEntity> categories = new HashSet<>();

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

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public Set<CategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategoryEntity> categories) {
        this.categories = categories != null ? categories : new HashSet<>();
    }

    // ============== MÉTODOS DE CONVENIENCIA ==============

    /**
     * Agrega una categoría al producto
     */
    public void addCategory(CategoryEntity category) {
        this.categories.add(category);
    }

    /**
     * Remueve una categoría del producto
     */
    public void removeCategory(CategoryEntity category) {
        this.categories.remove(category);
    }

    /**
     * Limpia todas las categorías
     */
    public void clearCategories() {
        this.categories.clear();

    }
}
