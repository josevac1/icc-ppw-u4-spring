package ec.edu.ups.icc.fundamentos01.products.services;

import java.util.List;
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
        dto.id = entity.getId();
        dto.name = entity.getName();
        dto.price = entity.getPrice();
        dto.description = entity.getDescription();
        dto.stock = entity.getStock();
        
        // Solo asignar fechas si existen
        if (entity.getCreatedAt() != null) {
            dto.createdAt = entity.getCreatedAt();
        }
        if (entity.getUpdatedAt() != null) {
            dto.updatedAt = entity.getUpdatedAt();
        }

        // Información del usuario (propietario)
        ProductResponseDto.UserSummaryDto userDto = new ProductResponseDto.UserSummaryDto();
        userDto.id = entity.getOwner().getId();
        userDto.name = entity.getOwner().getName();
        userDto.email = entity.getOwner().getEmail();
        dto.user = userDto;

        // Información de la categoría
        ProductResponseDto.CategoryResponseDto categoryDto = new ProductResponseDto.CategoryResponseDto();
        categoryDto.id = entity.getCategory().getId();
        categoryDto.name = entity.getCategory().getName();
        categoryDto.description = entity.getCategory().getDescription();
        dto.category = categoryDto;

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

        return productRepo.findByCategoryId(categoryId)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public List<ProductResponseDto> findByOwnerName(String ownerName) {
        if (ownerName == null || ownerName.isBlank()) {
            throw new BadRequestException("El nombre del propietario es obligatorio");
        }

        return productRepo.findByOwnerName(ownerName)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public List<ProductResponseDto> findByCategoryName(String categoryName) {
        if (categoryName == null || categoryName.isBlank()) {
            throw new BadRequestException("El nombre de la categoría es obligatorio");
        }

        return productRepo.findByCategoryName(categoryName)
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

        return productRepo.findByCategoryIdAndPriceGreaterThan(categoryId, minPrice)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public ProductResponseDto update(Long id, UpdateProductDto dto) {

        // 1. BUSCAR PRODUCTO EXISTENTE
        ProductEntity existing = productRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con ID: " + id));

        // 2. VALIDAR NUEVA CATEGORÍA (si cambió)
        CategoryEntity newCategory = categoryRepo.findById(dto.categoryId)
                .orElseThrow(() -> new NotFoundException("Categoría no encontrada con ID: " + dto.categoryId));

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

        // 4. CONVERTIR A ENTIDAD MANTENIENDO OWNER ORIGINAL
        ProductEntity updated = product.toEntity(existing.getOwner(), newCategory);
        updated.setId(id); // Asegurar que mantiene el ID

        // 5. PERSISTIR Y RESPONDER
        ProductEntity saved = productRepo.save(updated);
        return toResponseDto(saved);
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
        
        ProductEntity updated = product.toEntity(entity.getOwner(), entity.getCategory());
        updated.setId(entity.getId());
        
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

        ProductEntity updated = product.toEntity(entity.getOwner(), entity.getCategory());
        updated.setId(entity.getId());
        ProductEntity saved = productRepo.save(updated);

        return toResponseDto(saved);
    }

    @Override
    public ProductResponseDto create(CreateProductDto dto) {

        // 1. VALIDAR EXISTENCIA DE RELACIONES
        UserEntity owner = userRepo.findById(dto.userId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + dto.userId));

        CategoryEntity category = categoryRepo.findById(dto.categoryId)
                .orElseThrow(() -> new NotFoundException("Categoría no encontrada con ID: " + dto.categoryId));

        // Regla: nombre único
        if (productRepo.findByName(dto.name).isPresent()) {
            throw new IllegalStateException("El nombre del producto ya está registrado");
        }

        // 2. CREAR MODELO DE DOMINIO
        Product product = Product.fromDto(dto);

        // 3. CONVERTIR A ENTIDAD CON RELACIONES
        ProductEntity entity = product.toEntity(owner, category);

        // 4. PERSISTIR
        ProductEntity saved = productRepo.save(entity);

        // 5. CONVERTIR A DTO DE RESPUESTA
        return toResponseDto(saved);
    }
}
