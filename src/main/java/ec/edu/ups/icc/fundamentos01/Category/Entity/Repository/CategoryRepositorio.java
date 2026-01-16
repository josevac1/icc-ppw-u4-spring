package ec.edu.ups.icc.fundamentos01.Category.Entity.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ec.edu.ups.icc.fundamentos01.Category.Entity.CategoryEntity;


@Repository
public interface CategoryRepositorio extends JpaRepository<CategoryEntity, Long> {

    boolean existsByName(String name);

    /**
     * Busca categoría por nombre (case insensitive)
     */
    Optional<CategoryEntity> findByNameIgnoreCase(String name);

}
