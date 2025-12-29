package ec.edu.ups.icc.fundamentos01.products.dtos;

public class UpdateProductDto {
    public String name;
    public String description;
    public Double price;

    // Getters
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public Double getPrice() {
        return price;
    }
    
    // Setters
    public void setName(String name) {
        this.name = name;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
}
