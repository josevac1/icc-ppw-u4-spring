package ec.edu.ups.icc.fundamentos01.users.controllers;

import ec.edu.ups.icc.fundamentos01.users.dtos.CreateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.UpdateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.PartialUpdateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.UserResponseDto;

import ec.edu.ups.icc.fundamentos01.users.services.UserService;
import ec.edu.ups.icc.fundamentos01.products.services.ProductService;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UserService service;
    private final ProductService productService;

    public UsersController(UserService service, ProductService productService) {
        this.service = service;
        this.productService = productService;
    }

    @GetMapping
    public List<UserResponseDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public UserResponseDto findOne(@PathVariable("id") int id) {
        return service.findOne(id);
    }

    // Subcolección: productos de un usuario
    @GetMapping("/{id}/products")
    public ResponseEntity<List<ProductResponseDto>> getProductsByUserId(@PathVariable("id") int id) {
        List<ProductResponseDto> products = productService.findByUserId(Long.valueOf(id));
        return ResponseEntity.ok(products);
    }

    /**
     * Endpoint: GET /api/users/{id}/products-v2
     * 
     * Obtiene los productos de un usuario específico con filtros opcionales.
     * Los filtros se aplican a nivel de base de datos para mejor rendimiento.
     * 
     * Parámetros de consulta opcionales:
     * - name: Nombre del producto (búsqueda parcial)
     * - minPrice: Precio mínimo
     * - maxPrice: Precio máximo
     * - categoryId: ID de la categoría
     * 
     * Ejemplo: GET /api/users/5/products-v2?name=laptop&minPrice=500&maxPrice=2000&categoryId=3
     * 
     * @param id ID del usuario
     * @param name Nombre del producto (opcional)
     * @param minPrice Precio mínimo (opcional)
     * @param maxPrice Precio máximo (opcional)
     * @param categoryId ID de la categoría (opcional)
     * @return Lista de productos que cumplen con los criterios
     */
    @GetMapping("/{id}/products-v2")
    public ResponseEntity<List<ProductResponseDto>> getProductsByUserIdWithFilters(
            @PathVariable("id") Long id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @RequestParam(value = "categoryId", required = false) Long categoryId) {
        
        List<ProductResponseDto> products = service.getProductsByUserIdWithFilters(id, name, minPrice, maxPrice, categoryId);
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody CreateUserDto dto) {
        UserResponseDto created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(@PathVariable("id") int id,
            @Valid @RequestBody UpdateUserDto dto) {
        UserResponseDto updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> partialUpdate(@PathVariable("id") int id,
            @Valid @RequestBody PartialUpdateUserDto dto) {
        UserResponseDto updated = service.partialUpdate(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}