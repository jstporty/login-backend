package com.example.loginbackend.controller

import com.example.loginbackend.dto.LoginRequest
import com.example.loginbackend.dto.SignupRequest
import com.example.loginbackend.dto.UserResponse
import com.example.loginbackend.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"], methods = [RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS])
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/register")
    fun register(@Valid @RequestBody request: SignupRequest): ResponseEntity<Any> {
        println("🔵 회원가입 요청 받음: ${request.username}, ${request.email}")
        return try {
            val user = authService.register(request)
            println("✅ 회원가입 성공: ${user.username}")
            ResponseEntity.ok(user)
        } catch (e: IllegalArgumentException) {
            println("❌ 회원가입 실패: ${e.message}")
            ResponseEntity.badRequest().body(mapOf("message" to e.message))
        }
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<Any> {
        println("🔵 로그인 요청 받음: ${request.username}")
        val user = authService.login(request)
        return if (user != null) {
            println("✅ 로그인 성공: ${user.username}")
            ResponseEntity.ok(user)
        } else {
            println("❌ 로그인 실패: 잘못된 인증 정보")
            ResponseEntity.status(401).body(mapOf("message" to "잘못된 인증 정보"))
        }
    }
}
