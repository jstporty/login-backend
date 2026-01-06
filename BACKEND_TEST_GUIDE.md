# 백엔드 API 테스트 자동화 가이드

## 📋 개요
엑셀 파일에 작성된 테스트 케이스를 읽어서 **백엔드 API를 직접 호출**하여 테스트하고, 결과를 엑셀로 저장합니다.

## 🎯 백엔드에서 할 수 있는 것

1. ✅ 엑셀 파일에서 테스트 케이스 읽기
2. ✅ WebClient로 백엔드 API 직접 호출 (HTTP 요청)
3. ✅ 응답 검증 (상태코드, 결과)
4. ✅ 결과를 엑셀 파일로 자동 저장

## 📁 설정

### application.yml
```yaml
test:
  automation:
    input-file: /Users/mz02-horang/c드라이브/test_cases.xlsx
    output-directory: /Users/mz02-horang/c드라이브
```

### 엑셀 파일 형식

**위치**: `/Users/mz02-horang/c드라이브/test_cases.xlsx`

**헤더**:
```
TestType | Username | Password | Email | ExpectedStatus | ExpectedResult | Description
```

**예시**:
```
REGISTER | testuser01 | Password1234! | test@example.com | 200 | SUCCESS | 정상 회원가입
LOGIN    | testuser01 | Password1234! |                  | 200 | SUCCESS | 정상 로그인
REGISTER | ab         | Test123!      | invalid@test.com | 400 | FAIL    | username 짧음
```

## 🚀 실행 방법

### 1단계: 백엔드 서버 실행
```bash
cd /Users/mz02-horang/c드라이브/login-backend

# IntelliJ에서 Run 또는
./gradlew bootRun
```

### 2단계: API 테스트 실행

**기본 경로 사용 (파라미터 없음):**
```bash
curl -X POST http://localhost:8080/api/test/execute
```

**커스텀 경로 사용:**
```bash
curl -X POST "http://localhost:8080/api/test/execute?inputFilePath=/Users/mz02-horang/c드라이브/my_test.xlsx"
```

**Postman 사용:**
- Method: POST
- URL: `http://localhost:8080/api/test/execute`

## 📊 동작 방식

1. **엑셀 파일 읽기**: 테스트 케이스 로드
2. **API 호출**: 
   - `POST /api/auth/register` - 회원가입 테스트
   - `POST /api/auth/login` - 로그인 테스트
3. **결과 검증**: 
   - 상태코드 비교 (200, 400, 401 등)
   - 성공/실패 판단
4. **결과 저장**: 
   - 파일명: `test_results_20260106_153045.xlsx`
   - 위치: `/Users/mz02-horang/c드라이브/`

## 📝 응답 예시

```json
{
  "totalTests": 10,
  "passedTests": 9,
  "failedTests": 1,
  "results": [
    {
      "rowNumber": 2,
      "testType": "REGISTER",
      "username": "testuser01",
      "description": "정상 회원가입",
      "expectedStatus": 200,
      "actualStatus": 200,
      "expectedResult": "SUCCESS",
      "actualResult": "SUCCESS",
      "passed": true,
      "responseMessage": "{\"id\":1,\"username\":\"testuser01\",\"email\":\"test@example.com\"}",
      "executedAt": "2026-01-06 15:30:45"
    }
  ],
  "outputFilePath": "/Users/mz02-horang/c드라이브/test_results_20260106_153045.xlsx"
}
```

## 🎯 테스트 케이스 예시 (10개)

```tsv
TestType	Username	Password	Email	ExpectedStatus	ExpectedResult	Description
REGISTER	testuser01	Password1234!	testuser01@example.com	200	SUCCESS	정상 회원가입 - 모든 조건 만족
REGISTER	abc	Test12345678	abc@test.com	200	SUCCESS	username 최소길이(3자) 테스트
REGISTER	user20chars12345678	MyPass1234	longuser@test.com	200	SUCCESS	username 최대길이(20자) 테스트
REGISTER	validUser123	12345678	valid@mail.com	200	SUCCESS	password 최소길이(8자) 테스트
REGISTER	numberUser999	SecurePass99!	num999@example.com	200	SUCCESS	username 숫자포함 테스트
REGISTER	UPPERCASE123	UpperPass123	upper@test.com	200	SUCCESS	username 대문자 테스트
LOGIN	testuser01	Password1234!		200	SUCCESS	정상 로그인 - 가입된 계정
LOGIN	abc	Test12345678		200	SUCCESS	정상 로그인 - 짧은 username
LOGIN	validUser123	12345678		200	SUCCESS	정상 로그인 - 최소 password
REGISTER	test-user	Password123!	invalid@test.com	400	FAIL	username 특수문자 포함으로 실패
```

## 📞 API 엔드포인트

### 테스트 실행
- **URL**: `POST /api/test/execute`
- **Parameters** (선택):
  - `inputFilePath`: 엑셀 파일 경로
  - `outputDirectory`: 결과 저장 경로

### 설정 확인
- **URL**: `GET /api/test/config`
- **응답**:
```json
{
  "defaultInputFile": "/Users/mz02-horang/c드라이브/test_cases.xlsx",
  "defaultOutputDirectory": "/Users/mz02-horang/c드라이브"
}
```

### 헬스체크
- **URL**: `GET /api/test/health`
- **응답**: `{"status": "ok"}`

## 💡 특징

### ✅ 장점
- 브라우저 없이 빠른 테스트
- CI/CD 파이프라인에 통합 가능
- 서버 간 통신으로 안정적
- 결과를 엑셀로 자동 저장

### ⚠️ 제약
- 실제 화면은 보이지 않음 (API만 테스트)
- 프론트엔드 검증 불가
- JavaScript 동작 검증 불가

## 🔧 기술 스택

- **Apache POI**: 엑셀 파일 읽기/쓰기
- **Spring WebFlux (WebClient)**: 비동기 HTTP 클라이언트
- **Kotlin**: 간결한 코드

## 📌 주의사항

1. **서버 실행 필수**: 백엔드 서버가 `localhost:8080`에서 실행 중이어야 함
2. **테스트 순서**: 엑셀 순서대로 실행되므로 회원가입 → 로그인 순서 고려
3. **중복 실행**: 같은 username으로 여러 번 회원가입 시 중복 오류 발생
4. **DB 상태**: 테스트 전 DB 상태 확인 (필요시 초기화)

## 🎯 MCP 연동

MCP에서 다음과 같이 호출:
```
POST http://localhost:8080/api/test/execute
```

자동으로:
1. 엑셀 파일 읽기
2. API 호출
3. 결과 검증
4. 엑셀 저장
5. 요약 리턴

