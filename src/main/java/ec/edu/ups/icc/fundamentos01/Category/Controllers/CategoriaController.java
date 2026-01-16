package ec.edu.ups.icc.fundamentos01.Category.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.ups.icc.fundamentos01.Category.dto.CategoryResponseDTO;
import ec.edu.ups.icc.fundamentos01.Category.dto.CategoryRequestDTO;
import ec.edu.ups.icc.fundamentos01.Category.services.CategoryService;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;



@RestController
@RequestMapping("/api/categories")
public class CategoriaController {
    private CategoryService service;

    public CategoriaController(CategoryService service) {
        this.service = service;
    }

    /**
     * Crear una nueva categoría
     * POST /api/categories
     */
    @PostMapping
    public ResponseEntity<String> save(@RequestBody CategoryRequestDTO dto) {
        service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Categoría creada exitosamente");
    }

    /**
     * Obtener todas las categorías
     * GET /api/categories
     */
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> findAll() {
        List<CategoryResponseDTO> categories = service.findAll();
        return ResponseEntity.ok(categories);
    }

    /**
     * Obtener una categoría por ID
     * GET /api/categories/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> findById(@PathVariable Long id) {
        CategoryResponseDTO category = service.findById(id);
        return ResponseEntity.ok(category);
    }

    /**
     * Actualizar una categoría
     * PUT /api/categories/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody CategoryRequestDTO dto) {
        service.update(id, dto);
        return ResponseEntity.ok("Categoría actualizada exitosamente");
    }

    /**
     * Eliminar una categoría
     * DELETE /api/categories/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Categoría eliminada exitosamente");
    }
}
