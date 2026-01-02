package ec.edu.ups.icc.fundamentos01.exception.base;

import org.springframework.http.HttpStatus;

/**
 * Excepción base de la aplicación.
 * Todas las excepciones personalizadas del sistema deben heredar de esta clase.
 * 
 * Esta clase:
 * - Es la raíz de todas las excepciones del sistema
 * - Obliga a definir un HttpStatus
 * - Evita el uso de RuntimeException genérica
 * - No genera respuesta HTTP directamente
 */
public abstract class ApplicationException extends RuntimeException {

    private final HttpStatus status;

    protected ApplicationException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
