# 테스트 가이드

## 📋 개요

이 문서는 Login Backend 프로젝트의 테스트 방법을 설명합니다.
수동 테스트와 MCP를 활용한 자동화 테스트 방법을 다룹니다.

## 🧪 테스트 유형

### 1. 수동 테스트 (Manual Testing)
- curl, Postman, 또는 프론트엔드 UI 사용
- 개발 중 빠른 확인용

### 2. 자동화 테스트 (Automated Testing)
- File MCP + Browser MCP 활용
- 회원가입/로그인 시나리오 자동 실행
- 엑셀 리포트 자동 생성

## 📁 테스트 관련 파일

```
login-backend/
├── test-cases-input.csv              # 테스트 케이스 정의
├── browser_full_test_korean_v2.py    # 자동화 테스트 스크립트 (최신)
└── test_results_korean_*.xlsx        # 테스트 결과 (자동 생성)
```

## 🎯 테스트 케이스

### 테스트 케이스 파일 구조 (`test-cases-input.csv`)

```csv
TestType,Username,Password,Email,ExpectedStatus,ExpectedResult,Description
REGISTER,testuser01,Password1234!,testuser01@example.com,200,SUCCESS,정상 회원가입 - 모든 조건 만족
REGISTER,abc,Test12345678,abc@test.com,200,SUCCESS,username 최소길이(3자) 테스트
REGISTER,user20chars12345678,MyPass1234,longuser@test.com,200,SUCCESS,username 최대길이(20자) 테스트
REGISTER,validUser123,12345678,valid@mail.com,200,SUCCESS,password 최소길이(8자) 테스트
REGISTER,numberUser999,SecurePass99!,num999@example.com,200,SUCCESS,username 숫자포함 테스트
REGISTER,UPPERCASE123,UpperPass123,upper@test.com,200,SUCCESS,username 대문자 테스트
LOGIN,testuser01,Password1234!,,200,SUCCESS,정상 로그인 - 가입된 계정
LOGIN,abc,Test12345678,,200,SUCCESS,정상 로그인 - 짧은 username
LOGIN,validUser123,12345678,,200,SUCCESS,정상 로그인 - 최소 password
REGISTER,test-user,Password123!,invalid@test.com,400,FAIL,username 특수문자 포함으로 실패
```

### 테스트 시나리오

#### ✅ 성공 케이스 (6개)
1. 정상 회원가입 - 모든 조건 만족
2. username 최소길이(3자) 테스트
3. username 최대길이(20자) 테스트
4. password 최소길이(8자) 테스트
5. username 숫자포함 테스트
6. username 대문자 테스트

#### ✅ 로그인 테스트 (3개)
7. 정상 로그인 - 가입된 계정
8. 정상 로그인 - 짧은 username
9. 정상 로그인 - 최소 password

#### ❌ 실패 케이스 (1개)
10. username 특수문자 포함으로 실패

## 🚀 자동화 테스트 실행

### 사전 준비

1. **백엔드 서버 실행**
```bash
cd /Users/mz02-horang/cdrive/login-backend
./gradlew bootRun
```

2. **프론트엔드 서버 실행**
```bash
cd /Users/mz02-horang/cdrive/login-frontend
npm run dev
```

3. **브라우저 준비**
- Chrome 또는 Edge 실행
- `http://localhost:5173` 탭 열기
- Browser Extension MCP가 제어 가능한 상태 유지

### 테스트 실행

```bash
# 1. 데이터베이스 초기화
mysql -u root -proot -e "USE login_backend; TRUNCATE TABLE users;"

# 2. 자동화 테스트 실행
python3 browser_full_test_korean_v2.py
```

### 실행 과정

1. **File MCP**: CSV 파일에서 테스트 케이스 읽기
2. **API 테스트**: 백엔드 API 직접 호출하여 테스트 실행
3. **결과 수집**: 각 테스트의 성공/실패 및 에러 메시지 기록
4. **엑셀 생성**: 한국어 테스트 리포트 자동 생성

## 📊 테스트 결과

### 엑셀 리포트 구조

생성된 엑셀 파일은 다음 컬럼을 포함합니다:

| 컬럼명 | 설명 | 예시 |
|--------|------|------|
| 번호 | 테스트 번호 | 1, 2, 3... |
| 테스트유형 | 회원가입/로그인 | 회원가입 |
| 사용자명 | 입력한 username | testuser01 |
| 이메일 | 입력한 email | test@example.com |
| 비밀번호 | 입력한 password | Password123! |
| 설명 | 테스트 설명 | 정상 회원가입 - 모든 조건 만족 |
| 예상결과 | 예상 결과 | 성공 |
| 실제결과 | 실제 결과 | 성공 |
| 판정 | PASS/FAIL | ✅ PASS |
| 실패사유 | 실패 이유 (한국어) | 사용자명은 영문과 숫자만 가능합니다 |

### 결과 해석

#### ✅ PASS
- 예상 결과와 실제 결과가 일치
- 성공 케이스가 성공하거나, 실패 케이스가 예상대로 실패

#### ❌ FAIL
- 예상 결과와 실제 결과가 불일치
- 실패사유 컬럼에 상세 한국어 에러 메시지 표시

### 출력 파일 예시

```
test_results_korean_20260107_141007.xlsx
```

파일명 형식: `test_results_korean_YYYYMMDD_HHMMSS.xlsx`

## 🔍 수동 테스트 방법

### curl 사용

#### 회원가입 테스트
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "Password123!"
  }'
```

**예상 응답 (성공)**:
```json
{
  "id": 1,
  "username": "testuser",
  "email": "test@example.com",
  "role": "USER"
}
```

**예상 응답 (실패 - 유효성 검증)**:
```json
{
  "message": "유효성 검증 실패",
  "errors": {
    "username": "사용자명은 3~20자여야 합니다"
  }
}
```

#### 로그인 테스트
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "Password123!"
  }'
```

**예상 응답 (성공)**:
```json
{
  "id": 1,
  "username": "testuser",
  "email": "test@example.com",
  "role": "USER"
}
```

**예상 응답 (실패)**:
```json
{
  "message": "잘못된 인증 정보"
}
```

## 🐛 테스트 문제 해결

### 1. "잘못된 인증 정보" 에러
**원인**: 회원가입된 사용자가 DB에 없음

**해결**:
```bash
# DB 확인
mysql -u root -proot -e "USE login_backend; SELECT * FROM users;"

# DB 초기화 후 다시 테스트
mysql -u root -proot -e "USE login_backend; TRUNCATE TABLE users;"
```

### 2. "Connection refused" 에러
**원인**: 백엔드 또는 프론트엔드 서버가 실행되지 않음

**해결**:
```bash
# 백엔드 확인
curl http://localhost:8080/api/auth/register

# 프론트엔드 확인
curl http://localhost:5173
```

### 3. Browser MCP 연결 안됨
**원인**: Chrome/Edge 브라우저가 열려있지 않거나 탭이 없음

**해결**:
- Chrome 또는 Edge 실행
- `http://localhost:5173` 탭 열기
- 브라우저를 다른 작업으로 사용하지 않기

### 4. 엑셀 파일 생성 안됨
**원인**: openpyxl 라이브러리가 설치되지 않음

**해결**:
```bash
pip install openpyxl
```

## 📝 테스트 케이스 추가하기

### 1. CSV 파일 수정

`test-cases-input.csv`에 새로운 행 추가:

```csv
REGISTER,newuser,NewPass123,new@test.com,200,SUCCESS,새로운 테스트 케이스
```

### 2. 테스트 재실행

```bash
mysql -u root -proot -e "USE login_backend; TRUNCATE TABLE users;"
python3 browser_full_test_korean_v2.py
```

## 🎯 테스트 전략

### 커버리지 목표

1. **회원가입**
   - ✅ 정상 케이스
   - ✅ 경계값 테스트 (최소/최대 길이)
   - ✅ 유효성 검증 실패 케이스
   - ✅ 중복 사용자명 케이스

2. **로그인**
   - ✅ 정상 로그인
   - ✅ 잘못된 비밀번호
   - ✅ 존재하지 않는 사용자

### 권장 테스트 주기

- **개발 중**: 기능 변경 시마다 수동 테스트
- **커밋 전**: 전체 자동화 테스트 실행
- **배포 전**: 전체 시나리오 검증

## 🔗 관련 문서

- [API_SPECIFICATION.md](API_SPECIFICATION.md) - API 상세 스펙
- [MCP_AUTOMATION.md](MCP_AUTOMATION.md) - MCP 자동화 상세 가이드
- [DATABASE_SETUP.md](DATABASE_SETUP.md) - DB 초기화 방법

