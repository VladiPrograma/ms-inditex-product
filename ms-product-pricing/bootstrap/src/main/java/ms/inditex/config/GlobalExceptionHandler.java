package ms.inditex.config;

import jakarta.validation.ConstraintViolationException;
import liquibase.snapshot.InvalidExampleException;
import ms.inditex.adapters.output.persistence.exceptions.EntityNotFoundException;
import ms.inditex.exeptions.AppException;
import ms.inditex.vo.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEntityNotFound(EntityNotFoundException ex) {
        log.warn("Entity not found: {}", ex.getMessage(), ex);
        return new ErrorResponse("ENTITY_NOT_FOUND", ex.getMessage(), List.of());
    }

    @ExceptionHandler(InvalidExampleException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidExample(InvalidExampleException ex) {
        log.info("Invalid example request: {}", ex.getMessage(), ex);
        return new ErrorResponse("INVALID_EXAMPLE", ex.getMessage(), List.of());
    }

    @ExceptionHandler(AppException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAppException(AppException ex) {
        log.error("Unexpected application error: {}", ex.getMessage(), ex);
        String causeMessage = Optional.ofNullable(ex.getCause()).map(Throwable::getMessage).orElse("");
        return new ErrorResponse("INTERNAL_ERROR", ex.getMessage(), List.of(causeMessage));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneralException(Exception ex) {
        log.error("Unhandled exception occurred", ex);
        return new ErrorResponse(
                "UNEXPECTED_ERROR", "An unexpected failure occurred", List.of(ex.getMessage()));
    }

    /**
     * Catches Bean Validation errors on @Valid @RequestBody DTOs.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> details =
                ex.getBindingResult().getFieldErrors().stream()
                        .map(err -> err.getField() + ": " + err.getDefaultMessage())
                        .collect(Collectors.toList());

        return new ErrorResponse("VALIDATION_FAILED", "Request validation failed", details);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolations(ConstraintViolationException ex) {
        List<String> details =
                ex.getConstraintViolations().stream()
                        .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                        .collect(Collectors.toList());

        return new ErrorResponse("VALIDATION_FAILED", "Request parameter validation failed", details);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleWebExchangeBind(WebExchangeBindException ex) {
        List<String> details =
                ex.getFieldErrors().stream()
                        .map(err -> err.getField() + ": " + err.getDefaultMessage())
                        .collect(Collectors.toList());

        return new ErrorResponse("VALIDATION_FAILED", "Request validation failed", details);
    }

    @ExceptionHandler(ServerWebInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleWebInput(ServerWebInputException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof WebExchangeBindException bindEx) {
            return handleWebExchangeBind(bindEx);
        }
        return new ErrorResponse(
                "MALFORMED_REQUEST",
                "Could not parse request body",
                List.of(ex.getReason() != null ? ex.getReason() : ex.getMessage()));
    }
}
