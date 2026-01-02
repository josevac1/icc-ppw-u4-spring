package ec.edu.ups.icc.fundamentos01.products.services;

import java.util.List;
import org.springframework.stereotype.Service;
import ec.edu.ups.icc.fundamentos01.exception.domain.ConflictException;
import ec.edu.ups.icc.fundamentos01.exception.domain.NotFoundException;
import ec.edu.ups.icc.fundamentos01.products.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.PartialUpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.UpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import ec.edu.ups.icc.fundamentos01.products.mappers.ProductMapper;
import ec.edu.ups.icc.fundamentos01.products.models.Product;
import ec.edu.ups.icc.fundamentos01.products.repositories.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;

    public ProductServiceImpl(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public List<ProductResponseDto> findAll() {
        return productRepo.findAll()
                .stream()
                .map(Product::fromEntity)
                .map(ProductMapper::toResponse)
                .toList();
    }

    @Override
    public ProductResponseDto findOne(int id) {
        return productRepo.findById((long) id)
                .map(Product::fromEntity)
                .map(ProductMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con ID: " + id));
    }

    @Override
    public ProductResponseDto create(CreateProductDto dto) {

        // Regla: nombre único
        if (productRepo.findByName(dto.getName()).isPresent()) {
            throw new ConflictException("El nombre del producto '" + dto.getName() + "' ya está registrado");
        }

        Product product = Product.fromDto(dto);

        ProductEntity saved = productRepo.save(product.toEntity());

        return ProductMapper.toResponse(Product.fromEntity(saved));
    }

    @Override
    public ProductResponseDto update(int id, UpdateProductDto dto) {
        return productRepo.findById((long) id)
                .map(Product::fromEntity)
                .map(p -> p.update(dto))
                .map(Product::toEntity)
                .map(productRepo::save)
                .map(Product::fromEntity)
                .map(ProductMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con ID: " + id));
    }

    @Override
    public ProductResponseDto partialUpdate(int id, PartialUpdateProductDto dto) {
        return productRepo.findById((long) id)
                .map(Product::fromEntity)
                .map(p -> p.partialUpdate(dto))
                .map(Product::toEntity)
                .map(productRepo::save)
                .map(Product::fromEntity)
                .map(ProductMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con ID: " + id));
    }

    @Override
    public void delete(int id) {
        productRepo.findById((long) id)
                .ifPresentOrElse(productRepo::delete,
                        () -> {
                            throw new NotFoundException("Producto no encontrado con ID: " + id);
                        });
    }
}
