package com.example.loginbackend.mcp

import com.example.loginbackend.dto.TestCase
import com.example.loginbackend.dto.TestResult
import org.springframework.stereotype.Service
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

@Service
class PuppeteerMcpService {

    private val puppeteerScript = """
const puppeteer = require('puppeteer');

(async () => {
    const browser = await puppeteer.launch({
        headless: false,  // 브라우저 보이게
        args: ['--start-maximized']
    });
    
    const page = await browser.newPage();
    await page.setViewport({ width: 1920, height: 1080 });
    
    // STDIN에서 명령 읽기
    process.stdin.setEncoding('utf8');
    
    process.stdin.on('data', async (data) => {
        try {
            const command = JSON.parse(data);
            let result = {};
            
            switch(command.action) {
                case 'navigate':
                    await page.goto(command.url, { waitUntil: 'networkidle2' });
                    result = { success: true, message: 'Navigated to ' + command.url };
                    break;
                    
                case 'input':
                    await page.waitForSelector(command.selector, { timeout: 5000 });
                    await page.click(command.selector);
                    await page.type(command.selector, command.value, { delay: 100 });
                    result = { success: true, message: 'Input completed' };
                    break;
                    
                case 'click':
                    await page.waitForSelector(command.selector, { timeout: 5000 });
                    await page.click(command.selector);
                    result = { success: true, message: 'Clicked ' + command.selector };
                    break;
                    
                case 'getText':
                    await page.waitForSelector(command.selector, { timeout: 5000 });
                    const text = await page.${'$'}eval(command.selector, el => el.textContent);
                    result = { success: true, elementText: text };
                    break;
                    
                case 'wait':
                    await page.waitForTimeout(command.value || 1000);
                    result = { success: true, message: 'Waited' };
                    break;
                    
                case 'close':
                    await browser.close();
                    result = { success: true, message: 'Browser closed' };
                    process.exit(0);
                    break;
            }
            
            console.log('RESULT:' + JSON.stringify(result));
        } catch (error) {
            console.log('ERROR:' + JSON.stringify({ success: false, message: error.message }));
        }
    });
})();
"""

    private var puppeteerProcess: Process? = null

    /**
     * Puppeteer 프로세스 시작
     */
    fun startPuppeteer(): Boolean {
        try {
            // puppeteer 스크립트 파일 생성
            val scriptFile = File("/tmp/puppeteer_mcp.js")
            scriptFile.writeText(puppeteerScript)
            
            // Node.js로 Puppeteer 실행
            val processBuilder = ProcessBuilder("node", scriptFile.absolutePath)
            processBuilder.redirectErrorStream(true)
            
            puppeteerProcess = processBuilder.start()
            
            Thread.sleep(2000) // 브라우저 시작 대기
            
            return true
        } catch (e: Exception) {
            println("❌ Puppeteer 시작 실패: ${e.message}")
            return false
        }
    }

    /**
     * Puppeteer에 명령 전송
     */
    fun sendCommand(command: BrowserCommand): BrowserResult {
        return try {
            if (puppeteerProcess == null || !puppeteerProcess!!.isAlive) {
                return BrowserResult(false, "Puppeteer 프로세스가 실행 중이지 않습니다")
            }
            
            // 명령을 JSON으로 변환해서 STDIN으로 전송
            val json = """{"action":"${command.action}","selector":"${command.selector}","value":"${command.value}","url":"${command.url}"}"""
            puppeteerProcess!!.outputStream.write((json + "\n").toByteArray())
            puppeteerProcess!!.outputStream.flush()
            
            // 결과 읽기
            Thread.sleep(500)
            val output = puppeteerProcess!!.inputStream.readAllBytes().toString(Charsets.UTF_8)
            
            BrowserResult(true, "명령 전송 완료")
        } catch (e: Exception) {
            BrowserResult(false, "명령 실행 실패: ${e.message}")
        }
    }

    /**
     * 브라우저 테스트 실행
     */
    fun executeTestWithBrowser(testCase: TestCase, frontendUrl: String): TestResult {
        val executedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        
        return try {
            when (testCase.testType) {
                "REGISTER" -> executeRegisterTest(testCase, frontendUrl, executedAt)
                "LOGIN" -> executeLoginTest(testCase, frontendUrl, executedAt)
                else -> createErrorResult(testCase, "알 수 없는 테스트 타입", executedAt)
            }
        } catch (e: Exception) {
            createErrorResult(testCase, "테스트 실행 중 오류: ${e.message}", executedAt)
        }
    }

    private fun executeRegisterTest(testCase: TestCase, frontendUrl: String, executedAt: String): TestResult {
        // 페이지 이동
        sendCommand(BrowserCommand(action = "navigate", url = frontendUrl))
        Thread.sleep(1000)
        
        // 회원가입 폼 입력
        sendCommand(BrowserCommand(action = "input", selector = "#reg-username", value = testCase.username))
        sendCommand(BrowserCommand(action = "input", selector = "#reg-email", value = testCase.email))
        sendCommand(BrowserCommand(action = "input", selector = "#reg-password", value = testCase.password))
        
        // 회원가입 버튼 클릭
        sendCommand(BrowserCommand(action = "click", selector = "#registerForm button[type='submit']"))
        Thread.sleep(1500)
        
        // 결과 메시지 읽기
        val result = sendCommand(BrowserCommand(action = "getText", selector = "#register-message"))
        val isSuccess = result.elementText?.contains("성공") == true
        
        val actualStatus = if (isSuccess) 200 else 400
        val actualResult = if (isSuccess) "SUCCESS" else "FAIL"
        
        return TestResult(
            rowNumber = testCase.rowNumber,
            testType = testCase.testType,
            username = testCase.username,
            email = testCase.email,
            description = testCase.description,
            expectedStatus = testCase.expectedStatus,
            actualStatus = actualStatus,
            expectedResult = testCase.expectedResult,
            actualResult = actualResult,
            passed = actualStatus == testCase.expectedStatus,
            responseMessage = result.elementText,
            executedAt = executedAt
        )
    }

    private fun executeLoginTest(testCase: TestCase, frontendUrl: String, executedAt: String): TestResult {
        // 로그인 폼 입력
        sendCommand(BrowserCommand(action = "input", selector = "#login-username", value = testCase.username))
        sendCommand(BrowserCommand(action = "input", selector = "#login-password", value = testCase.password))
        
        // 로그인 버튼 클릭
        sendCommand(BrowserCommand(action = "click", selector = "#loginForm button[type='submit']"))
        Thread.sleep(1500)
        
        // 결과 메시지 읽기
        val result = sendCommand(BrowserCommand(action = "getText", selector = "#login-message"))
        val isSuccess = result.elementText?.contains("성공") == true
        
        val actualStatus = if (isSuccess) 200 else 401
        val actualResult = if (isSuccess) "SUCCESS" else "FAIL"
        
        return TestResult(
            rowNumber = testCase.rowNumber,
            testType = testCase.testType,
            username = testCase.username,
            email = testCase.email,
            description = testCase.description,
            expectedStatus = testCase.expectedStatus,
            actualStatus = actualStatus,
            expectedResult = testCase.expectedResult,
            actualResult = actualResult,
            passed = actualStatus == testCase.expectedStatus,
            responseMessage = result.elementText,
            executedAt = executedAt
        )
    }

    private fun createErrorResult(testCase: TestCase, message: String, executedAt: String): TestResult {
        return TestResult(
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
            responseMessage = message,
            executedAt = executedAt
        )
    }

    /**
     * Puppeteer 프로세스 종료
     */
    fun stopPuppeteer() {
        sendCommand(BrowserCommand(action = "close"))
        puppeteerProcess?.destroy()
        puppeteerProcess = null
    }
}

