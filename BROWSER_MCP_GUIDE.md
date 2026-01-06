# 브라우저 MCP 테스트 자동화 가이드

## 🎯 개요
**Puppeteer를 사용한 브라우저 MCP**로 크롬 브라우저에서 실제 화면을 보면서 테스트가 자동으로 실행됩니다.

## 📋 사전 준비

### 1. Node.js 설치 확인
```bash
node --version
npm --version
```

### 2. Puppeteer 설치
```bash
# 전역 설치
npm install -g puppeteer

# 또는 프로젝트 디렉토리에 설치
cd /Users/mz02-horang/c드라이브/login-backend
npm init -y
npm install puppeteer
```

### 3. 프론트엔드 서버 실행
```bash
# 프론트엔드 레포지토리에서
npm start  # 보통 http://localhost:3000
```

### 4. 백엔드 서버 실행
```bash
# IntelliJ에서 LoginBackendApplication 실행
```

## 🚀 실행 방법

### 브라우저 MCP 테스트 실행
```bash
curl -X POST http://localhost:8080/api/test/execute-browser-mcp
```

**또는 Postman:**
- Method: POST
- URL: `http://localhost:8080/api/test/execute-browser-mcp`

## 🎬 동작 과정

1. **Puppeteer 프로세스 시작** → 크롬 브라우저 자동 열림
2. **프론트엔드 URL 접속** (`http://localhost:3000`)
3. **엑셀 테스트 케이스 순서대로 실행:**
   - Username 입력 (타이핑 애니메이션)
   - Email 입력
   - Password 입력
   - 회원가입 버튼 클릭
   - 결과 메시지 읽기
   - 로그인 폼 입력
   - 로그인 버튼 클릭
4. **5초 대기 후 브라우저 자동 닫힘**
5. **결과 엑셀 저장**: `browser_mcp_results_20260106_153045.xlsx`

## 📊 MCP 통신 구조

```
Backend (Kotlin)
    ↓
PuppeteerMcpService
    ↓ (STDIN/STDOUT)
Node.js Puppeteer Process
    ↓
Chrome Browser (CDP)
    ↓
Frontend (http://localhost:3000)
```

## 🎨 지원되는 MCP 명령어

### navigate
```json
{
  "action": "navigate",
  "url": "http://localhost:3000"
}
```

### input (타이핑)
```json
{
  "action": "input",
  "selector": "#reg-username",
  "value": "testuser01"
}
```

### click
```json
{
  "action": "click",
  "selector": "#registerForm button"
}
```

### getText (결과 읽기)
```json
{
  "action": "getText",
  "selector": "#register-message"
}
```

### wait
```json
{
  "action": "wait",
  "value": "1000"
}
```

### close
```json
{
  "action": "close"
}
```

## 📝 프론트엔드 HTML 요구사항

### 회원가입 폼
```html
<input id="reg-username" />
<input id="reg-email" />
<input id="reg-password" />
<form id="registerForm">
  <button type="submit">회원가입</button>
</form>
<div id="register-message"></div>
```

### 로그인 폼
```html
<input id="login-username" />
<input id="login-password" />
<form id="loginForm">
  <button type="submit">로그인</button>
</form>
<div id="login-message"></div>
```

## 📞 API 엔드포인트

### 1. 브라우저 MCP 테스트 (크롬에서 보임)
```
POST /api/test/execute-browser-mcp
```

### 2. API 직접 테스트 (브라우저 없음)
```
POST /api/test/execute
```

### 3. 설정 확인
```
GET /api/test/config
```

응답:
```json
{
  "defaultInputFile": "/Users/mz02-horang/c드라이브/test_cases.xlsx",
  "defaultOutputDirectory": "/Users/mz02-horang/c드라이브",
  "frontendUrl": "http://localhost:3000"
}
```

## 🎯 특징

### ✅ 장점
- 실제 브라우저에서 동작 확인 가능
- 타이핑 애니메이션 (100ms delay)
- 실제 사용자처럼 동작
- 스크린샷 캡처 가능 (확장 가능)

### ⚠️ 주의사항
1. **Node.js 필수**: Puppeteer는 Node.js 환경 필요
2. **프론트엔드 실행 필수**: `http://localhost:3000` 접속 가능해야 함
3. **포트 충돌 주의**: 백엔드 8080, 프론트엔드 3000
4. **크롬 설치 필수**: Puppeteer가 크롬 사용

## 🔧 트러블슈팅

### Puppeteer 시작 실패
```bash
# Puppeteer 재설치
npm install puppeteer

# 또는 Chromium 수동 다운로드
npx puppeteer browsers install chrome
```

### 포트 변경 필요 시
`application.yml`:
```yaml
test:
  automation:
    frontend-url: http://localhost:5173  # Vite 등
```

## 💡 확장 기능

추가 가능한 기능:
- 📸 **스크린샷 자동 저장**
- 📹 **비디오 녹화**
- 🎭 **여러 브라우저 지원** (Firefox, Safari)
- ⚡ **병렬 실행**
- 🔄 **재시도 로직**

---

**이제 크롬 브라우저에서 실제로 입력하고 클릭하는 모습을 눈으로 확인할 수 있습니다!** 🎉

