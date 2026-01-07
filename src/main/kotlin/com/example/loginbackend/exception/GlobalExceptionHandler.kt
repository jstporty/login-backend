package com.example.loginbackend.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, Any>> {
        val errors = mutableMapOf<String, String>()
        
        ex.bindingResult.fieldErrors.forEach { error ->
            errors[error.field] = error.defaultMessage ?: "유효성 검증 오류"
        }
        
        val response = mapOf(
            "message" to "유효성 검증 실패",
            "errors" to errors
        )
        
        println("❌ Validation 실패: $errors")
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
    }
    
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<Map<String, Any>> {
        val response = mapOf(
            "message" to (ex.message ?: "잘못된 요청")
        )
        
        println("❌ IllegalArgumentException: ${ex.message}")
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
    }
}

