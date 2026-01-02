package ec.edu.ups.icc.fundamentos01.exception.domain;

import ec.edu.ups.icc.fundamentos01.exception.base.ApplicationException;
import org.springframework.http.HttpStatus;

/**
 * Excepción que representa un recurso no encontrado.
 * 
 * Cuándo usarla:
 * - Al buscar una entidad por ID y no se encuentra
 * - Al intentar actualizar o eliminar un recurso inexistente
 * - En operaciones que requieren que el recurso exista previamente
 * 
 * Dónde se usa:
 * - En servicios, dentro de métodos como findById(), update(), delete()
 * - Después de consultas a repositorios que retornan Optional.empty()
 * 
 * Ejemplo:
 * return productRepository.findById(id)
 * .orElseThrow(() -> new NotFoundException("Producto no encontrado con ID: " +
 * id));
 */
public class NotFoundException extends ApplicationException {

    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
