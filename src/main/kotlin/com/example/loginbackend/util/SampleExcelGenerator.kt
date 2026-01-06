package com.example.loginbackend.util

import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream

/**
 * 샘플 테스트 케이스 엑셀 파일 생성 유틸리티
 */
object SampleExcelGenerator {

    fun generateSampleTestCases(outputPath: String) {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Test Cases")

        // 헤더 생성
        val headerRow = sheet.createRow(0)
        val headers = listOf("TestType", "Email", "Password", "Name", "ExpectedStatus", "ExpectedResult", "Description")
        headers.forEachIndexed { index, header ->
            headerRow.createCell(index).setCellValue(header)
        }

        // 샘플 테스트 케이스
        val testCases = listOf(
            // 정상 회원가입 테스트
            listOf("REGISTER", "test1@example.com", "Test1234!", "홍길동", "200", "SUCCESS", "정상 회원가입 테스트"),
            listOf("REGISTER", "test2@example.com", "Password123!", "김철수", "200", "SUCCESS", "정상 회원가입 테스트 2"),
            
            // 이메일 형식 오류 테스트
            listOf("REGISTER", "invalid-email", "Test1234!", "이영희", "400", "FAIL", "이메일 형식 오류 테스트"),
            listOf("REGISTER", "", "Test1234!", "박민수", "400", "FAIL", "이메일 누락 테스트"),
            
            // 비밀번호 검증 오류 테스트
            listOf("REGISTER", "test3@example.com", "123", "최영수", "400", "FAIL", "비밀번호 짧음 테스트"),
            listOf("REGISTER", "test4@example.com", "", "정미래", "400", "FAIL", "비밀번호 누락 테스트"),
            
            // 중복 가입 테스트
            listOf("REGISTER", "test1@example.com", "Test1234!", "중복유저", "400", "FAIL", "중복 이메일 회원가입 시도"),
            
            // 정상 로그인 테스트
            listOf("LOGIN", "test1@example.com", "Test1234!", "", "200", "SUCCESS", "정상 로그인 테스트"),
            listOf("LOGIN", "test2@example.com", "Password123!", "", "200", "SUCCESS", "정상 로그인 테스트 2"),
            
            // 로그인 실패 테스트
            listOf("LOGIN", "test1@example.com", "WrongPassword!", "", "401", "FAIL", "잘못된 비밀번호 로그인"),
            listOf("LOGIN", "notexist@example.com", "Test1234!", "", "401", "FAIL", "존재하지 않는 계정 로그인"),
            listOf("LOGIN", "", "", "", "400", "FAIL", "빈 값으로 로그인 시도")
        )

        testCases.forEachIndexed { index, testCase ->
            val row = sheet.createRow(index + 1)
            testCase.forEachIndexed { cellIndex, value ->
                row.createCell(cellIndex).setCellValue(value)
            }
        }

        // 컬럼 너비 자동 조정
        headers.indices.forEach { sheet.autoSizeColumn(it) }

        // 파일 저장
        val file = File(outputPath)
        file.parentFile?.mkdirs()
        
        FileOutputStream(file).use { fos ->
            workbook.write(fos)
        }
        workbook.close()
        
        println("샘플 테스트 케이스 파일 생성 완료: $outputPath")
    }
}

// 샘플 파일 생성이 필요하면 이 함수를 실행하세요
// fun main() {
//     val outputPath = "/Users/mz02-horang/c드라이브/test_cases_sample.xlsx"
//     SampleExcelGenerator.generateSampleTestCases(outputPath)
// }

