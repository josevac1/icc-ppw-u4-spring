package ec.edu.ups.icc.fundamentos01.exception.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Contrato de respuesta de error estándar para toda la aplicación.
 * 
 * Esta clase define el único formato de error del sistema, soportando:
 * - Errores simples (sin details)
 * - Errores de validación (con details)
 * 
 * El campo details solo aparece cuando hay errores de validación múltiples
 * gracias a @JsonInclude(JsonInclude.Include.NON_NULL).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse implements Serializable {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private Map<String, String> details;

    /**
     * Constructor completo con details.
     * Se usa para errores de validación con múltiples campos inválidos.
     * 
     * @param status El HttpStatus del error
     * @param message Mensaje general del error
     * @param path La ruta del endpoint que generó el error
     * @param details Mapa de campo -> mensaje de error
     */
    public ErrorResponse(
            HttpStatus status,
            String message,
            String path,
            Map<String, String> details
    ) {
        this.timestamp = LocalDateTime.now();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.path = path;
        this.details = details;
    }

    /**
     * Constructor simplificado sin details.
     * Se usa para errores de dominio simples (recurso no encontrado, conflicto).
     * 
     * @param status El HttpStatus del error
     * @param message Mensaje general del error
     * @param path La ruta del endpoint que generó el error
     */
    public ErrorResponse(HttpStatus status, String message, String path) {
        this(status, message, path, null);
    }

    // Getters
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getDetails() {
        return details;
    }
}
