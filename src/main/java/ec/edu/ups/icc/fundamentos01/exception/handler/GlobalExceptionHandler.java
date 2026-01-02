package ec.edu.ups.icc.fundamentos01.exception.handler;

import ec.edu.ups.icc.fundamentos01.exception.base.ApplicationException;
import ec.edu.ups.icc.fundamentos01.exception.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Handler global de excepciones para toda la aplicación.
 * 
 * Maneja tres tipos de excepciones:
 * 1. ApplicationException - Excepciones de dominio personalizadas
 * 2. MethodArgumentNotValidException - Errores de validación de DTOs
 * 3. Exception - Cualquier otro error inesperado
 * 
 * Este handler garantiza:
 * - Un único formato de respuesta de error
 * - Manejo centralizado sin try/catch en controladores
 * - Respuestas consistentes para el cliente
 * - No exposición de información interna del servidor
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

        private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

        /**
         * Maneja todas las excepciones de dominio de la aplicación.
         * Convierte ApplicationException y sus subclases en respuestas HTTP
         * estructuradas.
         * 
         * @param ex      La excepción de aplicación lanzada
         * @param request La solicitud HTTP que generó el error
         * @return ResponseEntity con ErrorResponse
         */
        @ExceptionHandler(ApplicationException.class)
        public ResponseEntity<ErrorResponse> handleApplicationException(
                        ApplicationException ex,
                        HttpServletRequest request) {
                ErrorResponse response = new ErrorResponse(
                                ex.getStatus(),
                                ex.getMessage(),
                                request.getRequestURI());

                return ResponseEntity
                                .status(ex.getStatus())
                                .body(response);
        }

        /**
         * Maneja errores de validación de DTOs cuando se usa @Valid.
         * Extrae cada FieldError y los convierte en un mapa campo -> mensaje.
         * 
         * @param ex      La excepción de validación
         * @param request La solicitud HTTP que generó el error
         * @return ResponseEntity con ErrorResponse que incluye el campo details
         */
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidationException(
                        MethodArgumentNotValidException ex,
                        HttpServletRequest request) {
                Map<String, String> errors = new HashMap<>();

                // Extrae cada error de validación
                ex.getBindingResult()
                                .getFieldErrors()
                                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

                ErrorResponse response = new ErrorResponse(
                                HttpStatus.BAD_REQUEST,
                                "Datos de entrada inválidos",
                                request.getRequestURI(),
                                errors);

                return ResponseEntity
                                .badRequest()
                                .body(response);
        }

        /**
         * Maneja cualquier excepción no prevista.
         * Evita exponer detalles internos al cliente.
         * 
         * En producción, aquí se debería loggear el stacktrace completo
         * para debugging interno sin exponerlo al cliente.
         * 
         * @param ex      La excepción inesperada
         * @param request La solicitud HTTP que generó el error
         * @return ResponseEntity con ErrorResponse genérico
         */
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleUnexpectedException(
                        Exception ex,
                        HttpServletRequest request) {
                logger.error("Error inesperado en: " + request.getRequestURI(), ex);

                ErrorResponse response = new ErrorResponse(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "Error interno del servidor",
                                request.getRequestURI());

                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(response);
        }
}
