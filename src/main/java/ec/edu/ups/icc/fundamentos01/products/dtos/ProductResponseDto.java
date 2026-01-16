package ec.edu.ups.icc.fundamentos01.products.dtos;

import java.time.LocalDateTime;


public class ProductResponseDto {
    public Long id;
    public String name;
    public Double price;
    public String description;
    public int stock;

    // ============== OBJETOS ANIDADOS ==============
    
    public UserSummaryDto user;
    public CategoryResponseDto category;

    // ============== AUDITORÍA ==============
    
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    // ============== DTOs INTERNOS ==============
    
    public static class UserSummaryDto {
        public Long id;
        public String name;
        public String email;
    }

    public static class CategoryResponseDto {
        public Long id;
        public String name;
        public String description;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    
    public UserSummaryDto getUser() {
        return user;
    }

    public void setUser(UserSummaryDto user) {
        this.user = user;
    }
    

    public void setPrice(Double price) {
        this.price = price;
    }

    public CategoryResponseDto getCategory() {
        return category;
    }

    public void setCategory(CategoryResponseDto category) {
        this.category = category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
   
    
}