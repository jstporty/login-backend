package com.example.loginbackend.repository

import com.example.loginbackend.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository // Explicit import for clarity, though not strictly required with JpaRepository
import java.util.Optional

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): Optional<User>
    fun existsByUsername(username: String): Boolean
}
