package com.example.cosmo_cats_marketplace.util;

import com.example.cosmo_cats_marketplace.exception.controller.ValidationErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class ProblemDetailsUtil {

  private ProblemDetailsUtil() {}

  public static Map<String, Object> buildValidationErrorResponse(
          List<ValidationErrorDetails> errorDetails, String requestPath) {
    List<Map<String, Object>> formattedDetails = errorDetails.stream()
            .map(detail -> Map.<String, Object>of(
                    "parameterName", detail.getParameterName(),
                    "errorMessage", detail.getErrorMessage()))
            .toList();

    return Map.of(
            "status", HttpStatus.BAD_REQUEST.value(),
            "error", "Validation Error",
            "type", URI.create("/validation-error"),
            "path", requestPath,
            "invalidParams", formattedDetails
    );
  }


  public static ResponseEntity<Map<String, Object>> buildErrorResponse(
          HttpStatus status, String errorKey, WebRequest request, String message) {
    String requestPath = request.getDescription(false).replace("uri=", "");
    Map<String, Object> responseBody = Map.of(
            "status", status.value(),
            "error", errorKey,
            "type", URI.create("/" + errorKey),
            "path", requestPath,
            "message", message
    );
    return ResponseEntity.status(status).body(responseBody);
  }

  public static ResponseEntity<Map<String, Object>> buildUnexpectedErrorResponse(
          WebRequest request, String message) {
    String requestPath = request.getDescription(false).replace("uri=", "");
    Map<String, Object> responseBody = Map.of(
            "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "error", "Unexpected Error",
            "type", URI.create("/unexpected-error"),
            "path", requestPath,
            "message", message
    );
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
  }
}
