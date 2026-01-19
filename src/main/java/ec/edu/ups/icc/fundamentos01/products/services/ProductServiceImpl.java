package ec.edu.ups.icc.fundamentos01.products.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import ec.edu.ups.icc.fundamentos01.Category.Entity.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.Category.Entity.Repository.CategoryRepositorio;
import ec.edu.ups.icc.fundamentos01.exception.domain.BadRequestException;
import ec.edu.ups.icc.fundamentos01.exception.domain.ConflictException;
import ec.edu.ups.icc.fundamentos01.exception.domain.NotFoundException;
import ec.edu.ups.icc.fundamentos01.products.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.PartialUpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto.CategoryResponseDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.UpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.validateProductsUpdate;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import ec.edu.ups.icc.fundamentos01.products.models.Product;
import ec.edu.ups.icc.fundamentos01.products.repositories.ProductRepository;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import ec.edu.ups.icc.fundamentos01.users.repositories.UserRepository;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;

    private final UserRepository userRepo;
    private final CategoryRepositorio categoryRepo;

    public ProductServiceImpl(
            ProductRepository productRepo,
            UserRepository userRepo,
            CategoryRepositorio categoryRepo) {
        this.productRepo = productRepo;
        this.userRepo = userRepo;
        this.categoryRepo = categoryRepo;
    }

    private ProductResponseDto toResponseDto(ProductEntity entity) {
        ProductResponseDto dto = new ProductResponseDto();

        // Campos básicos
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPrice(entity.getPrice());
        dto.setDescription(entity.getDescription() != null ? entity.getDescription() : "");
        dto.setStock(entity.getStock());

        // Asignar fechas de auditoría
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        // Información del usuario (propietario)
        ProductResponseDto.UserSummaryDto ownerDto = new ProductResponseDto.UserSummaryDto();
        ownerDto.setId(entity.getOwner().getId());
        ownerDto.setName(entity.getOwner().getName());
        ownerDto.setEmail(entity.getOwner().getEmail());

        List<CategoryResponseDto> list = new java.util.ArrayList<>();
        for (CategoryEntity cat : entity.getCategories()) {
            CategoryResponseDto categoryDto = new CategoryResponseDto();
            categoryDto.setId(cat.getId());
            categoryDto.setName(cat.getName());
            categoryDto.setDescription(cat.getDescription() != null ? cat.getDescription() : "");
            list.add(categoryDto);
        }

        dto.setUser(ownerDto);
        dto.setCategories(list);
        return dto;
    }

    @Override
    public List<ProductResponseDto> findAll() {
        return productRepo.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public ProductResponseDto findOne(int id) {
        return productRepo.findById((long) id)
                .map(this::toResponseDto)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con ID: " + id));
    }

    @Override
    public ProductResponseDto findById(Long id) {
        return productRepo.findById(id)
                .map(this::toResponseDto)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con ID: " + id));
    }

    @Override
    public List<ProductResponseDto> findByUserId(Long userId) {

        // Validar que el usuario existe
        if (!userRepo.existsById(userId)) {
            throw new NotFoundException("Usuario no encontrado con ID: " + userId);
        }

        return productRepo.findByOwnerId(userId)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public List<ProductResponseDto> findByCategoryId(Long categoryId) {

        // Validar que la categoría existe
        if (!categoryRepo.existsById(categoryId)) {
            throw new NotFoundException("Categoría no encontrada con ID: " + categoryId);
        }

        return productRepo.findByCategoriesId(categoryId)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public List<ProductResponseDto> findByOwnerName(String ownerName) {
        if (ownerName == null || ownerName.isBlank()) {
            throw new BadRequestException("El nombre del propietario es obligatorio");
        }

        // Buscar todos los productos y filtrar por nombre del propietario
        return productRepo.findAll()
                .stream()
                .filter(p -> p.getOwner().getName().equalsIgnoreCase(ownerName))
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public List<ProductResponseDto> findByCategoryName(String categoryName) {
        if (categoryName == null || categoryName.isBlank()) {
            throw new BadRequestException("El nombre de la categoría es obligatorio");
        }

        return productRepo.findByCategoriesName(categoryName)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public List<ProductResponseDto> findByCategoryIdAndMinPrice(Long categoryId, Double minPrice) {
        if (categoryId == null || categoryId <= 0) {
            throw new BadRequestException("El ID de la categoría es inválido");
        }
        if (minPrice == null || minPrice < 0) {
            throw new BadRequestException("El precio mínimo debe ser 0 o mayor");
        }

        if (!categoryRepo.existsById(categoryId)) {
            throw new NotFoundException("Categoría no encontrada con ID: " + categoryId);
        }

        // Buscar productos que tengan esta categoría y el precio mínimo especificado
        return productRepo.findByCategoriesId(categoryId)
                .stream()
                .filter(p -> p.getPrice() >= minPrice)
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public ProductResponseDto update(Long id, UpdateProductDto dto) {

        // 1. BUSCAR PRODUCTO EXISTENTE
        ProductEntity existing = productRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con ID: " + id));

        // 2. VALIDAR Y OBTENER NUEVAS CATEGORÍAS
        Set<CategoryEntity> newCategories = validateAndGetCategories(dto.categoryIds);

        // 2.1 Validar nombre único si cambió
        if (dto.name != null && !existing.getName().equalsIgnoreCase(dto.name)) {
            productRepo.findByName(dto.name)
                    .ifPresent(other -> {
                        if (!other.getId().equals(id)) {
                            throw new ConflictException("Ya existe un producto con el nombre: " + dto.name);
                        }
                    });
        }

        // 3. ACTUALIZAR USANDO DOMINIO
        Product product = Product.fromEntity(existing);
        product.update(dto);

        // 4. ACTUALIZAR ENTIDAD
        ProductEntity updated = product.toEntity(existing.getOwner());
        updated.setId(id); // Asegurar que mantiene el ID

        // IMPORTANTE: Limpiar categorías existentes y asignar nuevas
        updated.clearCategories();
        updated.setCategories(newCategories);

        // 5. PERSISTIR Y RESPONDER
        ProductEntity saved = productRepo.save(updated);
        return toResponseDto(saved);
    }

    private Set<CategoryEntity> validateAndGetCategories(Set<Long> categoryIds) {
        Set<CategoryEntity> categories = new HashSet<>();

        for (Long categoryId : categoryIds) {
            CategoryEntity category = categoryRepo.findById(categoryId)
                    .orElseThrow(() -> new NotFoundException("Categoría no encontrada: " + categoryId));
            categories.add(category);
        }

        return categories;
    }

    @Override
    public void delete(Long id) {

        ProductEntity product = productRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con ID: " + id));

        // Eliminación física (también se puede implementar lógica)
        productRepo.delete(product);
    }

    @Override
    public ProductResponseDto partialUpdate(int id, PartialUpdateProductDto dto) {
        ProductEntity entity = productRepo.findById((long) id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con ID: " + id));

        Product product = Product.fromEntity(entity);
        product.partialUpdate(dto);

        ProductEntity updated = product.toEntity(entity.getOwner());
        updated.setId(entity.getId());

        // Mantener las categorías existentes
        updated.setCategories(entity.getCategories());

        ProductEntity saved = productRepo.save(updated);
        return toResponseDto(saved);
    }

    @Override
    public boolean validarName(int id, String name) {
        productRepo.findByName(name)
                .ifPresent(existing -> {
                    if (id == 0 || existing.getId() != id) {
                        throw new ConflictException("Ya existe un producto con el nombre: " + name);
                    }
                });
        return true;
    }

    @Override
    public ProductResponseDto secureProduc(int id, validateProductsUpdate dto) {
        ProductEntity entity = productRepo.findById((long) id)
                .orElseThrow(() -> new BadRequestException("Producto no encontrado"));

        if (dto.price != null && dto.price > 1000) {
            if (dto.reason == null || dto.reason.isBlank()) {
                throw new BadRequestException(
                        "Productos con precio mayor a 1000 requieren justificación");
            }
        }

        Product product = Product.fromEntity(entity);

        if (dto.name != null)
            product.setName(dto.name);
        if (dto.price != null)
            product.setPrice(dto.price);
        if (dto.description != null)
            product.setDescription(dto.description);

        ProductEntity updated = product.toEntity(entity.getOwner());
        updated.setId(entity.getId());
        updated.setCategories(entity.getCategories());
        ProductEntity saved = productRepo.save(updated);

        return toResponseDto(saved);
    }

    @Override
    public ProductResponseDto create(CreateProductDto dto) {

        UserEntity owner = userRepo.findById(dto.userId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + dto.userId));
        Set<CategoryEntity> categories = validateAndGetCategories(dto.categoryIds);
        if (productRepo.findByName(dto.name).isPresent()) {
            throw new IllegalStateException("El nombre del producto ya está registrado");
        }
        Product product = Product.fromDto(dto);
        ProductEntity entity = product.toEntity(owner);
        entity.setCategories(categories);
        ProductEntity saved = productRepo.save(entity);
        return toResponseDto(saved);
    }

    @Override
    public ProductResponseDto convertEntityToResponseDto(ProductEntity entity) {
        return toResponseDto(entity);
    }
}
