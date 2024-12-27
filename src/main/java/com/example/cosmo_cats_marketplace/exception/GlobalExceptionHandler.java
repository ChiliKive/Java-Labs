package com.example.cosmo_cats_marketplace.exception;

import com.example.cosmo_cats_marketplace.exception.controller.ValidationErrorDetails;
import com.example.cosmo_cats_marketplace.exception.service.CustomerNotFoundException;
import com.example.cosmo_cats_marketplace.exception.service.ProductAlreadyExistsException;
import com.example.cosmo_cats_marketplace.exception.service.ProductNotFoundException;
import com.example.cosmo_cats_marketplace.exception.service.ProductsNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.cosmo_cats_marketplace.util.ProblemDetailsUtil;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(CustomerNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleCustomerNotFound(
          CustomerNotFoundException ex, WebRequest request) {
    log.warn("Error: Customer not found - {}", ex.getMessage());
    return ProblemDetailsUtil.buildErrorResponse(
            HttpStatus.NOT_FOUND, "customer-not-found", request, ex.getMessage());
  }

  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleProductNotFound(
          ProductNotFoundException ex, WebRequest request) {
    log.warn("Error: Product not found - {}", ex.getMessage());
    return ProblemDetailsUtil.buildErrorResponse(
            HttpStatus.NOT_FOUND, "product-not-found", request, ex.getMessage());
  }

  @ExceptionHandler(ProductsNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleProductsNotFound(
          ProductsNotFoundException ex, WebRequest request) {
    log.warn("Error: Products not found - {}", ex.getMessage());
    return ProblemDetailsUtil.buildErrorResponse(
            HttpStatus.NOT_FOUND, "products-not-found", request, ex.getMessage());
  }

  @ExceptionHandler(ProductAlreadyExistsException.class)
  public ResponseEntity<Map<String, Object>> handleProductAlreadyExists(
          ProductAlreadyExistsException ex, WebRequest request) {
    log.error("Error: Product already exists - {}", ex.getMessage());
    return ProblemDetailsUtil.buildErrorResponse(
            HttpStatus.CONFLICT, "product-already-exists", request, ex.getMessage());
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ValidationErrorDetails> handleConstraintViolationException(ConstraintViolationException ex) {
    Map<String, String> validationErrors = ex.getConstraintViolations().stream()
            .collect(Collectors.toMap(
                    violation -> {
                      String fieldName = violation.getPropertyPath().toString();
                      return fieldName.substring(fieldName.lastIndexOf('.') + 1);
                    },
                    violation -> violation.getMessage(),
                    (existing, replacement) -> existing
            ));

    List<ValidationErrorDetails> details = validationErrors.entrySet().stream()
            .map(entry -> ValidationErrorDetails.builder()
                    .parameterName(entry.getKey())
                    .errorMessage(entry.getValue())
                    .build())
            .toList();

    return ResponseEntity.status(BAD_REQUEST).body(
            new ValidationErrorDetails("Validation Error", details.toString())
    );
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
          MethodArgumentNotValidException ex,
          HttpHeaders headers,
          HttpStatusCode status,
          WebRequest request) {
    List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

    Map<String, FieldError> filteredErrors = fieldErrors.stream()
            .collect(Collectors.toMap(
                    FieldError::getField,
                    error -> error,
                    (existing, replacement) -> {
                      if (existing.getCode().contains("NotBlank")) {
                        return existing;
                      }
                      return replacement;
                    }
            ));

    List<ValidationErrorDetails> validationResponse = filteredErrors.values().stream()
            .map(error -> ValidationErrorDetails.builder()
                    .parameterName(error.getField())
                    .errorMessage(error.getDefaultMessage())
                    .build())
            .toList();

    String path = request.getDescription(false).replace("uri=", "");
    log.warn("Error: Input params validation failed - {}", ex.getMessage());
    return ResponseEntity.status(BAD_REQUEST)
            .body(ProblemDetailsUtil.buildValidationErrorResponse(validationResponse, path));
  }



  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleGenericException(
          Exception ex, WebRequest request) {
    log.error("Unexpected error: {}", ex.getMessage(), ex);
    return ProblemDetailsUtil.buildUnexpectedErrorResponse(request, ex.getMessage());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, Object>> handleIllegalArgument(
          IllegalArgumentException ex, WebRequest request) {
    log.warn("Error: Invalid argument - {}", ex.getMessage());
    return ProblemDetailsUtil.buildErrorResponse(
            HttpStatus.BAD_REQUEST, "invalid-argument", request, ex.getMessage());
  }

  @ExceptionHandler(NumberFormatException.class)
  public ResponseEntity<Map<String, Object>> handleNumberFormatException(
          NumberFormatException ex, WebRequest request) {
    log.warn("Error: Invalid number format - {}", ex.getMessage());
    return ProblemDetailsUtil.buildErrorResponse(
            HttpStatus.BAD_REQUEST, "invalid-number-format", request, "Invalid number format provided");
  }
}