package com.example.loginbackend.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("*")  // 모든 Origin 허용
            .allowedMethods("*")  // 모든 메소드 허용
            .allowedHeaders("*")  // 모든 헤더 허용
            .maxAge(3600)
    }
}

