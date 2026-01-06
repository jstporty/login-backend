package com.example.loginbackend.dto

/**
 * 엑셀에서 읽어온 테스트 케이스 데이터
 */
data class TestCase(
    val rowNumber: Int,
    val testType: String, // REGISTER 또는 LOGIN
    val username: String?,
    val password: String?,
    val email: String?,
    val expectedStatus: Int, // 예상 HTTP 상태 코드 (200, 400, 401 등)
    val expectedResult: String, // SUCCESS 또는 FAIL
    val description: String // 테스트 설명
)

/**
 * 테스트 실행 결과
 */
data class TestResult(
    val rowNumber: Int,
    val testType: String,
    val username: String?,
    val email: String?,  // 추가
    val description: String,
    val expectedStatus: Int,
    val actualStatus: Int,
    val expectedResult: String,
    val actualResult: String,
    val passed: Boolean,
    val responseMessage: String?,
    val executedAt: String
)

/**
 * 전체 테스트 실행 결과 요약
 */
data class TestExecutionSummary(
    val totalTests: Int,
    val passedTests: Int,
    val failedTests: Int,
    val results: List<TestResult>,
    val outputFilePath: String
)

