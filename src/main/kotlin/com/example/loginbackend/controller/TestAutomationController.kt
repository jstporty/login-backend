package com.example.loginbackend.controller

import com.example.loginbackend.dto.TestExecutionSummary
import com.example.loginbackend.service.TestAutomationService
import com.example.loginbackend.service.ExcelTestService
import com.example.loginbackend.mcp.PuppeteerMcpService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
class TestAutomationController(
    private val testAutomationService: TestAutomationService,
    private val excelTestService: ExcelTestService,
    private val puppeteerMcpService: PuppeteerMcpService,
    @Value("\${test.automation.input-file}") private val defaultInputFile: String,
    @Value("\${test.automation.output-directory}") private val defaultOutputDirectory: String,
    @Value("\${test.automation.frontend-url}") private val frontendUrl: String
) {

    /**
     * 엑셀 기반 테스트 자동화 실행
     * 
     * @param inputFilePath 테스트 케이스가 작성된 엑셀 파일 경로 (선택, 기본값: /Users/mz02-horang/c드라이브/test_cases.xlsx)
     * @param outputDirectory 테스트 결과 엑셀을 저장할 디렉토리 (선택, 기본값: /Users/mz02-horang/c드라이브)
     * @return 테스트 실행 결과 요약
     * 
     * 예시 요청:
     * POST /api/test/execute (파라미터 없이 호출하면 기본 경로 사용)
     * POST /api/test/execute?inputFilePath=/Users/mz02-horang/c드라이브/my_test.xlsx
     */
    @PostMapping("/execute")
    fun executeTests(
        @RequestParam(required = false) inputFilePath: String?,
        @RequestParam(required = false) outputDirectory: String?
    ): ResponseEntity<TestExecutionSummary> {
        return try {
            // 파라미터가 없으면 기본값 사용
            val inputFile = inputFilePath ?: defaultInputFile
            val outputDir = outputDirectory ?: defaultOutputDirectory
            
            val summary = testAutomationService.executeTestsFromExcel(inputFile, outputDir)
            ResponseEntity.ok(summary)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(
                TestExecutionSummary(
                    totalTests = 0,
                    passedTests = 0,
                    failedTests = 0,
                    results = emptyList(),
                    outputFilePath = "오류: ${e.message}"
                )
            )
        } catch (e: Exception) {
            ResponseEntity.internalServerError().body(
                TestExecutionSummary(
                    totalTests = 0,
                    passedTests = 0,
                    failedTests = 0,
                    results = emptyList(),
                    outputFilePath = "예상치 못한 오류: ${e.message}"
                )
            )
        }
    }

    /**
     * 브라우저 MCP로 UI 테스트 실행 (Puppeteer)
     * 크롬 브라우저가 자동으로 열리고 실제 화면에서 테스트가 진행됩니다.
     */
    @PostMapping("/execute-browser-mcp")
    fun executeBrowserMcpTests(
        @RequestParam(required = false) inputFilePath: String?,
        @RequestParam(required = false) outputDirectory: String?
    ): ResponseEntity<TestExecutionSummary> {
        return try {
            val inputFile = inputFilePath ?: defaultInputFile
            val outputDir = outputDirectory ?: defaultOutputDirectory
            
            // 테스트 케이스 읽기
            val testCases = excelTestService.readTestCases(inputFile)
            
            if (testCases.isEmpty()) {
                throw IllegalArgumentException("테스트 케이스가 없습니다.")
            }
            
            // Puppeteer 시작 (크롬 브라우저 열림)
            val started = puppeteerMcpService.startPuppeteer()
            if (!started) {
                throw RuntimeException("Puppeteer 시작 실패. Node.js와 puppeteer가 설치되어 있는지 확인하세요.")
            }
            
            // 각 테스트 실행
            val results = testCases.map { testCase ->
                puppeteerMcpService.executeTestWithBrowser(testCase, frontendUrl)
            }
            
            // Puppeteer 종료
            Thread.sleep(5000) // 5초 대기 후 브라우저 닫기
            puppeteerMcpService.stopPuppeteer()
            
            // 결과 저장
            val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
            val outputFileName = "browser_mcp_results_$timestamp.xlsx"
            val outputPath = "$outputDir/$outputFileName"
            
            excelTestService.saveTestResults(results, outputPath)
            
            // 요약
            val passedTests = results.count { it.passed }
            val failedTests = results.count { !it.passed }
            
            ResponseEntity.ok(TestExecutionSummary(
                totalTests = results.size,
                passedTests = passedTests,
                failedTests = failedTests,
                results = results,
                outputFilePath = outputPath
            ))
        } catch (e: Exception) {
            ResponseEntity.internalServerError().body(
                TestExecutionSummary(
                    totalTests = 0,
                    passedTests = 0,
                    failedTests = 0,
                    results = emptyList(),
                    outputFilePath = "오류: ${e.message}"
                )
            )
        }
    }

    /**
     * 설정된 기본 파일 경로 확인
     */
    @GetMapping("/config")
    fun getConfig(): ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(mapOf(
            "defaultInputFile" to defaultInputFile,
            "defaultOutputDirectory" to defaultOutputDirectory,
            "frontendUrl" to frontendUrl
        ))
    }

    /**
     * 헬스체크 엔드포인트
     */
    @GetMapping("/health")
    fun health(): ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(mapOf("status" to "ok"))
    }
}
