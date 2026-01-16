package ec.edu.ups.icc.fundamentos01.products.controllers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PatchMapping;
import ec.edu.ups.icc.fundamentos01.products.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.UpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.PartialUpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.ValidateProductsNameDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.validateProductsUpdate;
import ec.edu.ups.icc.fundamentos01.products.services.ProductService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")

public class ProductsController {

		private final ProductService service;

    public ProductsController(ProductService productService) {
        this.service = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> create(@Valid @RequestBody CreateProductDto dto) {
        ProductResponseDto created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> findAll() {
        List<ProductResponseDto> products = service.findAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> findById(@PathVariable Long id) {
        ProductResponseDto product = service.findById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ProductResponseDto>> findByUserId(@PathVariable Long userId) {
        List<ProductResponseDto> products = service.findByUserId(userId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductResponseDto>> findByCategoryId(@PathVariable Long categoryId) {
        List<ProductResponseDto> products = service.findByCategoryId(categoryId);
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductDto dto
    ) {
        ProductResponseDto updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ============== ENDPOINTS ADICIONALES ==============

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDto> partialUpdate(
            @PathVariable Long id,
            @Valid @RequestBody PartialUpdateProductDto dto
    ) {
        ProductResponseDto updated = service.partialUpdate(id.intValue(), dto);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/validate-name")
    public ResponseEntity<String> validateName(@Valid @RequestBody ValidateProductsNameDto dto) {
        service.validarName(dto.id, dto.name);
        return ResponseEntity.ok("Nombre válido");
    }

    @PostMapping("/{id}/secure-update")
    public ResponseEntity<ProductResponseDto> secureUpdate(
            @PathVariable Long id,
            @Valid @RequestBody validateProductsUpdate dto
    ) {
        ProductResponseDto updated = service.secureProduc(id.intValue(), dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/owner/name/{name}")
    public ResponseEntity<List<ProductResponseDto>> findByOwnerName(@PathVariable String name) {
        List<ProductResponseDto> products = service.findByOwnerName(name);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/name/{name}")
    public ResponseEntity<List<ProductResponseDto>> findByCategoryName(@PathVariable String name) {
        List<ProductResponseDto> products = service.findByCategoryName(name);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{categoryId}/minprice/{price}")
    public ResponseEntity<List<ProductResponseDto>> findByCategoryAndMinPrice(
            @PathVariable Long categoryId,
            @PathVariable Double price
    ) {
        List<ProductResponseDto> products = service.findByCategoryIdAndMinPrice(categoryId, price);
        return ResponseEntity.ok(products);
    }
}
