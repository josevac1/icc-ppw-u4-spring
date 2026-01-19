package ec.edu.ups.icc.fundamentos01.users.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;

import ec.edu.ups.icc.fundamentos01.Category.Entity.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.core.entities.BaseModel;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;

@Entity
@Table(name = "users")
public class UserEntity extends BaseModel {
    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private Set<ProductEntity> products = new HashSet<>();
    

    public Set<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductEntity> products) {
        this.products = products != null ? products : new HashSet<>();
    }

    // Getters
    public String getName() {
        return name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getPassword() {
        return password;
    }
    
    // Setters
    public void setName(String name) {
        this.name = name;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

  
    // ============== MÉTODOS DE CONVENIENCIA ==============

    /**
     * Agrega una categoría al producto
     */
    public void addProduct(ProductEntity product) {
        this.products.add(product);
    }

    /**
     * Remueve una categoría del producto
     */
    public void removeProduct(ProductEntity product) {
        this.products.remove(product);
    }

    /**
     * Limpia todas las categorías
     */
    public void clearProducts() {
        this.products.clear();

    }
}
