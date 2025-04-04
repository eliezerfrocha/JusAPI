package com.example.api_base.API;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {
    // Getters
    private final String message;
    private final int status;
    private final T data;

    // Construtor privado para forçar o uso dos métodos estáticos
    private ApiResponse(String message, HttpStatus status, T data) {
        this.message = message;
        this.status = status.value();
        this.data = data;
    }

    // Métodos de sucesso
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(message, HttpStatus.OK, data);
    }

    public static ApiResponse<Void> success(String message) {
        return new ApiResponse<>(message, HttpStatus.OK, null);
    }

    public static <T> ApiResponse<T> created(String message, T data) {
        return new ApiResponse<>(message, HttpStatus.CREATED, data);
    }

    // Métodos de erro
    public static <T> ApiResponse<T> badRequest(String message) {
        return new ApiResponse<>(message, HttpStatus.BAD_REQUEST, null);
    }

    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(message, HttpStatus.NOT_FOUND, null);
    }

    public static <T> ApiResponse<T> internalServerError(String message) {
        return new ApiResponse<>(message, HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

}
