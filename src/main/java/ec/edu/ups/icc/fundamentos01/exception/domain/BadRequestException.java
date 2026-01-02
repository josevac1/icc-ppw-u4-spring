package ec.edu.ups.icc.fundamentos01.exception.domain;

import ec.edu.ups.icc.fundamentos01.exception.base.ApplicationException;
import org.springframework.http.HttpStatus;

/**
 * Excepción que representa una solicitud inválida (Bad Request).
 * 
 * Cuándo usarla:
 * - Cuando los datos son técnicamente válidos pero violan reglas de negocio
 * - Al detectar operaciones no permitidas según el estado actual del sistema
 * - Cuando se incumplen condiciones del dominio (stock insuficiente, saldo negativo, edad mínima)
 * - Para errores de validación que no son capturados por anotaciones de Bean Validation
 * - Cuando la estructura de los datos es correcta pero los valores no son aceptables
 * 
 * Dónde se usa:
 * - En servicios, dentro de la lógica de negocio y validaciones
 * - Después de validaciones específicas del dominio o del sistema
 * - En operaciones complejas que requieren verificar múltiples condiciones
 * - Como alternativa general a errores de validación no cubiertos por @Valid
 * 
 * Ejemplo:
 * if (product.getStock() < orderDto.getQuantity()) {
 *     throw new BadRequestException(
 *         "Stock insuficiente. Disponible: " + product.getStock() + 
 *         ", solicitado: " + orderDto.getQuantity()
 *     );
 * }
 */
public class BadRequestException extends ApplicationException {

    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
