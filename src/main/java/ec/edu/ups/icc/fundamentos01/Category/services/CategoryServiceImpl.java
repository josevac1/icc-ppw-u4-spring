package ec.edu.ups.icc.fundamentos01.Category.services;

import java.util.List;
import org.springframework.stereotype.Service;

import ec.edu.ups.icc.fundamentos01.Category.Entity.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.Category.Entity.Repository.CategoryRepositorio;
import ec.edu.ups.icc.fundamentos01.Category.dto.CategoryResponseDTO;
import ec.edu.ups.icc.fundamentos01.Category.dto.CategoryRequestDTO;
import ec.edu.ups.icc.fundamentos01.exception.domain.BadRequestException;
import ec.edu.ups.icc.fundamentos01.exception.domain.ConflictException;
import ec.edu.ups.icc.fundamentos01.exception.domain.NotFoundException;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepositorio repository;

    public CategoryServiceImpl(CategoryRepositorio repository) {
        this.repository = repository;
    }

    @Override
    public void save(CategoryRequestDTO dto) {
        // Validar que el nombre no esté vacío
        if (dto.name == null || dto.name.isBlank()) {
            throw new BadRequestException("El nombre de la categoría no puede estar vacío");
        }

        // Validar que el nombre no sea duplicado
        if (repository.existsByName(dto.name)) {
            throw new ConflictException("Ya existe una categoría con el nombre: " + dto.name);
        }

        CategoryEntity entity = new CategoryEntity();
        entity.setName(dto.name);
        entity.setDescription(dto.description);

        repository.save(entity);
    }

    @Override
    public List<CategoryResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(this::entityToDto)
                .toList();
    }

    @Override
    public CategoryResponseDTO findById(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("El ID de la categoría debe ser válido");
        }
        
        CategoryEntity entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoría no encontrada con ID: " + id));
        
        return entityToDto(entity);
    }

    @Override
    public void update(Long id, CategoryRequestDTO dto) {
        if (id == null || id <= 0) {
            throw new BadRequestException("El ID de la categoría debe ser válido");
        }
        
        if (dto.name == null || dto.name.isBlank()) {
            throw new BadRequestException("El nombre de la categoría no puede estar vacío");
        }

        CategoryEntity entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoría no encontrada con ID: " + id));

        // Verificar si el nuevo nombre ya existe en otra categoría
        if (!entity.getName().equalsIgnoreCase(dto.name) && repository.existsByName(dto.name)) {
            throw new ConflictException("Ya existe una categoría con el nombre: " + dto.name);
        }

        entity.setName(dto.name);
        entity.setDescription(dto.description);
        repository.save(entity);
    }

    @Override
    public void delete(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("El ID de la categoría debe ser válido");
        }

        if (!repository.existsById(id)) {
            throw new NotFoundException("Categoría no encontrada con ID: " + id);
        }

        repository.deleteById(id);
    }

    private CategoryResponseDTO entityToDto(CategoryEntity entity) {
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.id = entity.getId();
        dto.name = entity.getName();
        dto.description = entity.getDescription();
        return dto;
    }
}
