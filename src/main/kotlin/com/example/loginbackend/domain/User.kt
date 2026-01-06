package com.example.loginbackend.domain

import jakarta.persistence.*

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    var username: String,

    @Column(nullable = false)
    var password: String, // In a real app, this should be encrypted

    @Column
    var email: String? = null,
    
    @Column
    var role: String = "USER"
)
