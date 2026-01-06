# 엑셀 기반 API 테스트 자동화 가이드

## 📋 개요

이 시스템은 **`/Users/mz02-horang/c드라이브/test_cases.xlsx`** 파일에 작성된 테스트 케이스를 자동으로 읽어서 API 테스트를 실행하고, 같은 경로에 결과 파일을 저장합니다.

## 🎯 고정 경로 설정

### 기본 설정 (application.yml)
```yaml
test:
  automation:
    input-file: /Users/mz02-horang/c드라이브/test_cases.xlsx
    output-directory: /Users/mz02-horang/c드라이브
```

### 설정 확인
```bash
curl http://localhost:8080/api/test/config
```

**응답:**
```json
{
  "defaultInputFile": "/Users/mz02-horang/c드라이브/test_cases.xlsx",
  "defaultOutputDirectory": "/Users/mz02-horang/c드라이브"
}
```

## 🚀 사용 방법

### 1. 엑셀 테스트 케이스 파일 준비

엑셀 파일을 다음과 같은 형식으로 작성합니다:

| TestType | Email | Password | Name | ExpectedStatus | ExpectedResult | Description |
|----------|-------|----------|------|----------------|----------------|-------------|
| REGISTER | test@test.com | Test1234! | 홍길동 | 200 | SUCCESS | 정상 회원가입 |
| LOGIN | test@test.com | Test1234! | | 200 | SUCCESS | 정상 로그인 |
| REGISTER | invalid | Test1234! | 김철수 | 400 | FAIL | 이메일 형식 오류 |

#### 컬럼 설명:
- **TestType**: `REGISTER` (회원가입) 또는 `LOGIN` (로그인)
- **Email**: 테스트할 이메일 주소
- **Password**: 테스트할 비밀번호
- **Name**: 회원가입 시 사용할 이름 (LOGIN 타입에서는 빈 값)
- **ExpectedStatus**: 예상되는 HTTP 상태 코드 (200, 400, 401 등)
- **ExpectedResult**: 예상 결과 (`SUCCESS` 또는 `FAIL`)
- **Description**: 테스트 케이스 설명

### 2. 샘플 파일 생성 (선택사항)

샘플 테스트 케이스를 자동으로 생성하려면:

```bash
# Gradle로 샘플 생성 유틸리티 실행
./gradlew run --args="generate-sample"
```

또는 IDE에서 `SampleExcelGenerator.kt`의 `main` 함수를 실행하면  
`/Users/mz02-horang/c드라이브/test_cases_sample.xlsx` 파일이 생성됩니다.

### 3. 서버 실행

```bash
./gradlew bootRun
```

서버가 `http://localhost:8080`에서 실행됩니다.

### 4. 테스트 실행

#### 방법 1: 기본 경로 사용 (권장)

**가장 간단한 방법 - 파라미터 없이 호출:**
```bash
curl -X POST http://localhost:8080/api/test/execute
```

이 방법은 자동으로 `/Users/mz02-horang/c드라이브/test_cases.xlsx` 파일을 찾아서 테스트를 실행합니다.

**Postman 사용:**
- Method: POST
- URL: `http://localhost:8080/api/test/execute`
- Query Params: 없음 (기본값 사용)

#### 방법 2: 커스텀 경로 사용

다른 엑셀 파일을 사용하고 싶을 때:
```bash
curl -X POST "http://localhost:8080/api/test/execute?inputFilePath=/Users/mz02-horang/c드라이브/my_custom_test.xlsx"
```

**Postman 사용:**
- Method: POST
- URL: `http://localhost:8080/api/test/execute`
- Query Params:
  - `inputFilePath`: `/Users/mz02-horang/c드라이브/my_custom_test.xlsx`
  - `outputDirectory` (선택): `/Users/mz02-horang/c드라이브/results`

#### 방법 3: MCP를 통한 자동 실행

MCP(Model Context Protocol)를 사용하여:
1. MCP가 자동으로 `/api/test/execute`를 호출
2. 엑셀 파일을 읽어서 테스트 실행
3. 결과를 확인하고 화면에 표시

### 5. 테스트 결과 확인

테스트 실행 후 다음 정보가 반환됩니다:

```json
{
  "totalTests": 12,
  "passedTests": 8,
  "failedTests": 4,
  "results": [
    {
      "rowNumber": 2,
      "testType": "REGISTER",
      "email": "test1@example.com",
      "description": "정상 회원가입 테스트",
      "expectedStatus": 200,
      "actualStatus": 200,
      "expectedResult": "SUCCESS",
      "actualResult": "SUCCESS",
      "passed": true,
      "responseMessage": "{\"id\":1,\"email\":\"test1@example.com\",\"name\":\"홍길동\"}",
      "executedAt": "2026-01-06 15:30:45"
    }
  ],
  "outputFilePath": "/Users/mz02-horang/c드라이브/test_results_20260106_153045.xlsx"
}
```

결과 엑셀 파일은 자동으로 생성되며, 파일명에 실행 시간이 포함됩니다.

## 📊 결과 엑셀 파일 포맷

결과 파일에는 다음 컬럼이 포함됩니다:

| Row# | TestType | Email | Description | ExpectedStatus | ActualStatus | ExpectedResult | ActualResult | Passed | ResponseMessage | ExecutedAt |
|------|----------|-------|-------------|----------------|--------------|----------------|--------------|--------|-----------------|------------|

## ✅ 테스트 시나리오 예시

### 회원가입 테스트

1. **정상 케이스**: 유효한 이메일, 비밀번호, 이름 → 200 SUCCESS
2. **이메일 형식 오류**: 잘못된 이메일 형식 → 400 FAIL
3. **비밀번호 짧음**: 8자 미만 비밀번호 → 400 FAIL
4. **중복 이메일**: 이미 존재하는 이메일 → 400 FAIL
5. **필수값 누락**: 이메일/비밀번호/이름 누락 → 400 FAIL

### 로그인 테스트

1. **정상 케이스**: 올바른 이메일과 비밀번호 → 200 SUCCESS
2. **잘못된 비밀번호**: 틀린 비밀번호 → 401 FAIL
3. **존재하지 않는 계정**: 미등록 이메일 → 401 FAIL
4. **필수값 누락**: 이메일/비밀번호 누락 → 400 FAIL

## 🔧 설정 파일 (application.yml)

서버 포트를 변경하려면:

```yaml
server:
  port: 8080  # 원하는 포트로 변경
```

## 📝 주의사항

1. **테스트 순서**: 엑셀 파일의 순서대로 테스트가 실행되므로, 로그인 테스트 전에 회원가입이 필요한 경우 순서를 고려하세요.
2. **데이터베이스 초기화**: 테스트 전에 데이터베이스를 초기화하고 싶다면 `application.yml`의 `ddl-auto` 설정을 확인하세요.
3. **중복 실행**: 같은 이메일로 여러 번 회원가입 테스트를 실행하면 중복 오류가 발생할 수 있습니다.
4. **파일 경로**: 엑셀 파일 경로는 절대 경로를 사용하세요.

## 🛠️ 의존성

- **Apache POI**: 엑셀 파일 읽기/쓰기
- **Spring WebFlux**: HTTP 클라이언트로 API 호출
- **Jackson**: JSON 직렬화/역직렬화

## 📞 API 엔드포인트

### 1. 테스트 실행
- **URL**: `POST /api/test/execute`
- **Parameters** (모두 선택):
  - `inputFilePath`: 테스트 케이스 엑셀 파일 경로 (기본값: `/Users/mz02-horang/c드라이브/test_cases.xlsx`)
  - `outputDirectory`: 결과 파일 저장 경로 (기본값: `/Users/mz02-horang/c드라이브`)

### 2. 설정 확인
- **URL**: `GET /api/test/config`
- **Response**: 현재 설정된 기본 파일 경로

### 3. 헬스체크
- **URL**: `GET /api/test/health`
- **Response**: `{"status": "ok"}`

## 🎯 다음 단계

이 시스템을 더욱 확장하려면:

1. **스케줄링**: 주기적으로 자동 테스트 실행
2. **알림 기능**: 테스트 실패 시 이메일/슬랙 알림
3. **리포트 개선**: HTML 형식의 시각적인 리포트 생성
4. **병렬 실행**: 여러 테스트 케이스를 동시에 실행
5. **DB 스냅샷**: 테스트 전후 DB 상태 자동 백업/복원

