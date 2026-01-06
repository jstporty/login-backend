package com.example.loginbackend.service

import com.example.loginbackend.domain.User
import com.example.loginbackend.dto.LoginRequest
import com.example.loginbackend.dto.SignupRequest
import com.example.loginbackend.dto.UserResponse
import com.example.loginbackend.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userRepository: UserRepository
) {

    @Transactional
    fun register(request: SignupRequest): UserResponse {
        if (userRepository.existsByUsername(request.username)) {
            throw IllegalArgumentException("Username already exists")
        }

        val user = User(
            username = request.username,
            password = request.password,  // 비밀번호 평문 저장 (개발용)
            email = request.email
        )

        val savedUser = userRepository.save(user)

        return UserResponse(
            id = savedUser.id,
            username = savedUser.username,
            email = savedUser.email,
            role = savedUser.role
        )
    }

    fun login(request: LoginRequest): UserResponse? {
        val foundUser = userRepository.findByUsername(request.username)
        if (foundUser.isPresent) {
            val user = foundUser.get()
            if (request.password == user.password) {  // 평문 비교
                return UserResponse(
                    id = user.id,
                    username = user.username,
                    email = user.email,
                    role = user.role
                )
            }
        }
        return null
    }
}
