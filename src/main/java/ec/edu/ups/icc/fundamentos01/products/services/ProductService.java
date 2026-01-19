package ec.edu.ups.icc.fundamentos01.products.services;

import java.util.List;

import ec.edu.ups.icc.fundamentos01.products.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.PartialUpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.UpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.validateProductsUpdate;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;

public interface ProductService {

    List<ProductResponseDto> findAll();

    ProductResponseDto findOne(int id);

    ProductResponseDto create(CreateProductDto dto);

    ProductResponseDto update(Long id, UpdateProductDto dto);

    ProductResponseDto partialUpdate(int id, PartialUpdateProductDto dto);

    void delete(Long id);

    boolean validarName(int id, String name);

    ProductResponseDto secureProduc(int id, validateProductsUpdate dto);

    ProductResponseDto findById(Long id);

    List<ProductResponseDto> findByUserId(Long userId);

    List<ProductResponseDto> findByCategoryId(Long categoryId);

    // Búsquedas adicionales
    List<ProductResponseDto> findByOwnerName(String ownerName);

    List<ProductResponseDto> findByCategoryName(String categoryName);

    List<ProductResponseDto> findByCategoryIdAndMinPrice(Long categoryId, Double minPrice);

    /**
     * Convierte una entidad ProductEntity a ProductResponseDto.
     * Método público para uso en otras capas de la aplicación.
     * 
     * @param entity La entidad a convertir
     * @return DTO con la información del producto
     */
    ProductResponseDto convertEntityToResponseDto(ProductEntity entity);
}
