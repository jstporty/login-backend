package com.example.loginbackend.service

import com.example.loginbackend.dto.*
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class TestAutomationService(
    private val excelTestService: ExcelTestService,
    private val objectMapper: ObjectMapper,
    @Value("\${server.port:8080}") private val serverPort: Int
) {

    private val webClient = WebClient.builder()
        .baseUrl("http://localhost:$serverPort")
        .build()

    /**
     * 엑셀 파일의 모든 테스트 케이스 실행
     */
    fun executeTestsFromExcel(inputFilePath: String, outputDirectory: String): TestExecutionSummary {
        // 테스트 케이스 읽기
        val testCases = excelTestService.readTestCases(inputFilePath)
        
        if (testCases.isEmpty()) {
            throw IllegalArgumentException("테스트 케이스가 없습니다. 엑셀 파일을 확인해주세요.")
        }

        // 각 테스트 케이스 실행
        val results = testCases.map { testCase ->
            executeTestCase(testCase)
        }

        // 결과를 엑셀 파일로 저장
        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
        val outputFileName = "test_results_$timestamp.xlsx"
        val outputPath = "$outputDirectory/$outputFileName"
        
        excelTestService.saveTestResults(results, outputPath)

        // 요약 정보 생성
        val passedTests = results.count { it.passed }
        val failedTests = results.count { !it.passed }

        return TestExecutionSummary(
            totalTests = results.size,
            passedTests = passedTests,
            failedTests = failedTests,
            results = results,
            outputFilePath = outputPath
        )
    }

    /**
     * 단일 테스트 케이스 실행
     */
    private fun executeTestCase(testCase: TestCase): TestResult {
        val executedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        
        return try {
            when (testCase.testType) {
                "REGISTER" -> executeRegisterTest(testCase, executedAt)
                "LOGIN" -> executeLoginTest(testCase, executedAt)
                else -> {
                    TestResult(
                        rowNumber = testCase.rowNumber,
                        testType = testCase.testType,
                        username = testCase.username,
                        email = testCase.email,
                        description = testCase.description,
                        expectedStatus = testCase.expectedStatus,
                        actualStatus = 0,
                        expectedResult = testCase.expectedResult,
                        actualResult = "ERROR",
                        passed = false,
                        responseMessage = "알 수 없는 테스트 타입: ${testCase.testType}",
                        executedAt = executedAt
                    )
                }
            }
        } catch (e: Exception) {
            TestResult(
                rowNumber = testCase.rowNumber,
                testType = testCase.testType,
                username = testCase.username,
                email = testCase.email,
                description = testCase.description,
                expectedStatus = testCase.expectedStatus,
                actualStatus = 0,
                expectedResult = testCase.expectedResult,
                actualResult = "ERROR",
                passed = false,
                responseMessage = "테스트 실행 실패: ${e.message}",
                executedAt = executedAt
            )
        }
    }

    /**
     * 회원가입 테스트 실행
     */
    private fun executeRegisterTest(testCase: TestCase, executedAt: String): TestResult {
        val signupRequest = SignupRequest(
            username = testCase.username ?: "",
            password = testCase.password ?: "",
            email = testCase.email ?: ""
        )

        return try {
            val response = webClient.post()
                .uri("/api/auth/register")
                .bodyValue(signupRequest)
                .retrieve()
                .toEntity(String::class.java)
                .block()

            val actualStatus = response?.statusCode?.value() ?: 0
            val actualResult = if (actualStatus == 200) "SUCCESS" else "FAIL"
            val passed = actualStatus == testCase.expectedStatus && 
                        actualResult == testCase.expectedResult

            TestResult(
                rowNumber = testCase.rowNumber,
                testType = testCase.testType,
                username = testCase.username,
                email = testCase.email,
                description = testCase.description,
                expectedStatus = testCase.expectedStatus,
                actualStatus = actualStatus,
                expectedResult = testCase.expectedResult,
                actualResult = actualResult,
                passed = passed,
                responseMessage = response?.body,
                executedAt = executedAt
            )
        } catch (e: WebClientResponseException) {
            val actualStatus = e.statusCode.value()
            val actualResult = if (actualStatus == 200) "SUCCESS" else "FAIL"
            val passed = actualStatus == testCase.expectedStatus && 
                        actualResult == testCase.expectedResult

            TestResult(
                rowNumber = testCase.rowNumber,
                testType = testCase.testType,
                username = testCase.username,
                email = testCase.email,
                description = testCase.description,
                expectedStatus = testCase.expectedStatus,
                actualStatus = actualStatus,
                expectedResult = testCase.expectedResult,
                actualResult = actualResult,
                passed = passed,
                responseMessage = e.responseBodyAsString,
                executedAt = executedAt
            )
        }
    }

    /**
     * 로그인 테스트 실행
     */
    private fun executeLoginTest(testCase: TestCase, executedAt: String): TestResult {
        val loginRequest = LoginRequest(
            username = testCase.username ?: "",
            password = testCase.password ?: ""
        )

        return try {
            val response = webClient.post()
                .uri("/api/auth/login")
                .bodyValue(loginRequest)
                .retrieve()
                .toEntity(String::class.java)
                .block()

            val actualStatus = response?.statusCode?.value() ?: 0
            val actualResult = if (actualStatus == 200) "SUCCESS" else "FAIL"
            val passed = actualStatus == testCase.expectedStatus && 
                        actualResult == testCase.expectedResult

            TestResult(
                rowNumber = testCase.rowNumber,
                testType = testCase.testType,
                username = testCase.username,
                email = testCase.email,
                description = testCase.description,
                expectedStatus = testCase.expectedStatus,
                actualStatus = actualStatus,
                expectedResult = testCase.expectedResult,
                actualResult = actualResult,
                passed = passed,
                responseMessage = response?.body,
                executedAt = executedAt
            )
        } catch (e: WebClientResponseException) {
            val actualStatus = e.statusCode.value()
            val actualResult = if (actualStatus == 200) "SUCCESS" else "FAIL"
            val passed = actualStatus == testCase.expectedStatus && 
                        actualResult == testCase.expectedResult

            TestResult(
                rowNumber = testCase.rowNumber,
                testType = testCase.testType,
                username = testCase.username,
                email = testCase.email,
                description = testCase.description,
                expectedStatus = testCase.expectedStatus,
                actualStatus = actualStatus,
                expectedResult = testCase.expectedResult,
                actualResult = actualResult,
                passed = passed,
                responseMessage = e.responseBodyAsString,
                executedAt = executedAt
            )
        }
    }
}

