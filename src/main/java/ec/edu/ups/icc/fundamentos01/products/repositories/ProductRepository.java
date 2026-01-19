package ec.edu.ups.icc.fundamentos01.products.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;


@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByName(String name);

    // Búsquedas por relaciones
    List<ProductEntity> findByOwnerId(Long userId);
    
    /**
     * Encuentra productos que tienen UNA categoría específica
     * Útil para filtros de categoría
     */
    List<ProductEntity> findByCategoriesId(Long categoryId);
    
    /**
     * Encuentra productos que tienen una categoría con nombre específico
     */
    List<ProductEntity> findByCategoriesName(String categoryName);
    
    /**
     * Consulta personalizada: productos con TODAS las categorías especificadas
     */
    @Query("SELECT p FROM ProductEntity p " +
           "WHERE SIZE(p.categories) >= :categoryCount " +
           "AND :categoryCount = " +
           "(SELECT COUNT(c) FROM p.categories c WHERE c.id IN :categoryIds)")
    List<ProductEntity> findByAllCategories(@Param("categoryIds") List<Long> categoryIds,
                                          @Param("categoryCount") long categoryCount);
}