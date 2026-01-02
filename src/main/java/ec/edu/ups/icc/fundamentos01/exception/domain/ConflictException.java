package ec.edu.ups.icc.fundamentos01.exception.domain;

import ec.edu.ups.icc.fundamentos01.exception.base.ApplicationException;
import org.springframework.http.HttpStatus;

/**
 * Excepción que representa un conflicto de estado.
 * 
 * Cuándo usarla:
 * - Al intentar crear un recurso con un identificador único ya existente
 * (email, username, código)
 * - Cuando se intenta realizar una operación que violaría una restricción de
 * unicidad
 * - Al detectar conflictos de concurrencia o versiones
 * 
 * Dónde se usa:
 * - En servicios, dentro de métodos create() o register()
 * - Antes de persistir datos, validando unicidad
 * - En operaciones de registro de usuarios o creación de entidades con campos
 * únicos
 * 
 * Ejemplo:
 * if (userRepository.existsByEmail(userDto.getEmail())) {
 * throw new ConflictException("El email " + userDto.getEmail() + " ya está
 * registrado");
 * }
 */
public class ConflictException extends ApplicationException {

    public ConflictException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
