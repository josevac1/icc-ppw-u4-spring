package ec.edu.ups.icc.fundamentos01.Category.services;

import java.util.List;
import ec.edu.ups.icc.fundamentos01.Category.dto.CategoryResponseDTO;
import ec.edu.ups.icc.fundamentos01.Category.dto.CategoryRequestDTO;

public interface CategoryService {

    void save(CategoryRequestDTO dto);

    List<CategoryResponseDTO> findAll();

    CategoryResponseDTO findById(Long id);

    void update(Long id, CategoryRequestDTO dto);

    void delete(Long id);
}
