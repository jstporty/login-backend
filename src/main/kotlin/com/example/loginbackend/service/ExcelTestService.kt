package com.example.loginbackend.service

import com.example.loginbackend.dto.TestCase
import com.example.loginbackend.dto.TestResult
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class ExcelTestService {

    /**
     * 엑셀 파일에서 테스트 케이스 읽기
     * 
     * 엑셀 포맷:
     * Row 1 (헤더): TestType | Username | Password | Email | ExpectedStatus | ExpectedResult | Description
     * Row 2+: REGISTER | testuser01 | Test1234! | test@test.com | 200 | SUCCESS | 정상 회원가입
     */
    fun readTestCases(filePath: String): List<TestCase> {
        val file = File(filePath)
        if (!file.exists()) {
            throw IllegalArgumentException("엑셀 파일을 찾을 수 없습니다: $filePath")
        }

        val testCases = mutableListOf<TestCase>()
        
        FileInputStream(file).use { fis ->
            val workbook = WorkbookFactory.create(fis)
            val sheet = workbook.getSheetAt(0)
            
            // 첫 번째 행은 헤더이므로 건너뛰기
            for (i in 1..sheet.lastRowNum) {
                val row = sheet.getRow(i) ?: continue
                
                // 빈 행 건너뛰기
                if (isRowEmpty(row)) continue
                
                try {
                    val testCase = TestCase(
                        rowNumber = i + 1,
                        testType = getCellValueAsString(row.getCell(0)).uppercase(),
                        username = getCellValueAsString(row.getCell(1)),
                        password = getCellValueAsString(row.getCell(2)),
                        email = getCellValueAsString(row.getCell(3)),
                        expectedStatus = getCellValueAsString(row.getCell(4)).toIntOrNull() ?: 200,
                        expectedResult = getCellValueAsString(row.getCell(5)).uppercase(),
                        description = getCellValueAsString(row.getCell(6))
                    )
                    testCases.add(testCase)
                } catch (e: Exception) {
                    println("경고: ${i + 1}번째 행 파싱 실패 - ${e.message}")
                }
            }
            
            workbook.close()
        }
        
        return testCases
    }

    /**
     * 테스트 결과를 엑셀 파일로 저장
     */
    fun saveTestResults(results: List<TestResult>, outputPath: String) {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Test Results")
        
        // 헤더 생성
        val headerRow = sheet.createRow(0)
        val headers = listOf(
            "Row#", "TestType", "Username", "Email", "Description", 
            "ExpectedStatus", "ActualStatus", "ExpectedResult", "ActualResult",
            "Passed", "ResponseMessage", "ExecutedAt"
        )
        headers.forEachIndexed { index, header ->
            headerRow.createCell(index).setCellValue(header)
        }
        
        // 결과 데이터 작성
        results.forEachIndexed { index, result ->
            val row = sheet.createRow(index + 1)
            row.createCell(0).setCellValue(result.rowNumber.toDouble())
            row.createCell(1).setCellValue(result.testType)
            row.createCell(2).setCellValue(result.username ?: "")
            row.createCell(3).setCellValue(result.email ?: "")
            row.createCell(4).setCellValue(result.description)
            row.createCell(5).setCellValue(result.expectedStatus.toDouble())
            row.createCell(6).setCellValue(result.actualStatus.toDouble())
            row.createCell(7).setCellValue(result.expectedResult)
            row.createCell(8).setCellValue(result.actualResult)
            row.createCell(9).setCellValue(if (result.passed) "PASS" else "FAIL")
            row.createCell(10).setCellValue(result.responseMessage ?: "")
            row.createCell(11).setCellValue(result.executedAt)
        }
        
        // 컬럼 너비 자동 조정
        headers.indices.forEach { sheet.autoSizeColumn(it) }
        
        // 파일 저장
        FileOutputStream(outputPath).use { fos ->
            workbook.write(fos)
        }
        workbook.close()
    }

    private fun getCellValueAsString(cell: Cell?): String {
        if (cell == null) return ""
        
        return when (cell.cellType) {
            CellType.STRING -> cell.stringCellValue.trim()
            CellType.NUMERIC -> cell.numericCellValue.toInt().toString()
            CellType.BOOLEAN -> cell.booleanCellValue.toString()
            CellType.FORMULA -> cell.cellFormula
            else -> ""
        }
    }

    private fun isRowEmpty(row: Row): Boolean {
        for (cellNum in 0 until row.lastCellNum) {
            val cell = row.getCell(cellNum)
            if (cell != null && cell.cellType != CellType.BLANK && 
                getCellValueAsString(cell).isNotEmpty()) {
                return false
            }
        }
        return true
    }
}

