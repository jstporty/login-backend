# API Specification

이 문서는 Login Backend의 모든 API 엔드포인트 스펙을 정의합니다.

## Base URL
```
http://localhost:8080/api
```

## Authentication Endpoints

### 1. 회원가입 (Register)

**Endpoint**: `POST /auth/register`

**Request Body**:
```json
{
  "username": "string (required, 3-20자, 영문/숫자만)",
  "password": "string (required, 8-100자, 최소 1개 이상의 영문/숫자 포함)",
  "email": "string (required, 유효한 이메일 형식)"
}
```

**Success Response** (HTTP 200):
```json
{
  "id": 1,
  "username": "testuser",
  "email": "test@example.com",
  "role": "USER"
}
```

**Error Responses**:
- HTTP 400 Bad Request - 입력 검증 실패
  ```json
  {
    "message": "유효성 검증 실패",
    "errors": {
      "username": "사용자명은 3~20자여야 합니다",
      "password": "비밀번호는 최소 8자 이상이어야 합니다"
    }
  }
  ```
- HTTP 400 Bad Request - 중복 사용자명
  ```json
  {
    "message": "이미 존재하는 사용자명입니다"
  }
  ```

**유효성 검증 규칙**:
- **username**: 
  - 필수 (NotBlank)
  - 3~20자
  - 영문과 숫자만 허용
  - 에러 메시지: "사용자명은 3~20자여야 합니다", "사용자명은 영문과 숫자만 가능합니다"
- **email**: 
  - 필수 (NotBlank)
  - 올바른 이메일 형식
  - 에러 메시지: "올바른 이메일 형식이 아닙니다"
- **password**: 
  - 필수 (NotBlank)
  - 최소 8자 이상
  - 에러 메시지: "비밀번호는 최소 8자 이상이어야 합니다"

---

### 2. 로그인 (Login)

**Endpoint**: `POST /auth/login`

**Request Body**:
```json
{
  "username": "string (required)",
  "password": "string (required)"
}
```

**Success Response** (HTTP 200):
```json
{
  "id": 1,
  "username": "testuser",
  "email": "test@example.com",
  "role": "USER"
}
```

**Error Responses**:
- HTTP 401 Unauthorized - 잘못된 인증 정보
  ```json
  {
    "message": "잘못된 인증 정보"
  }
  ```

**유효성 검증 규칙**:
- **username**: 필수 (NotBlank)
- **password**: 필수 (NotBlank)

---

## Data Models

### User Entity
데이터베이스에 저장되는 사용자 엔티티:
```kotlin
{
  id: Long (PK, Auto-increment)
  username: String (Unique, Not Null)
  password: String (BCrypt 암호화, Not Null)
  email: String (Nullable)
  role: String (Default: "USER")
}
```

### Request DTOs
클라이언트에서 서버로 전송하는 데이터:
- `SignupRequest`: username, password, email
- `LoginRequest`: username, password

### Response DTOs
서버에서 클라이언트로 전송하는 데이터:
- `UserResponse`: id, username, email, role (비밀번호 제외)

---

## 보안 정책

1. **비밀번호 저장**: 평문 저장 (개발 환경 전용, 프로덕션에서는 BCrypt 사용 필요)
2. **비밀번호 응답 제외**: API 응답에 비밀번호 절대 포함하지 않음
3. **입력 검증**: Jakarta Validation 사용하여 서버 측 검증 수행
4. **한국어 에러 메시지**: 모든 에러 메시지는 한국어로 반환
5. **CORS 설정**: 개발 환경에서 모든 origin 허용 (`*`)

## 에러 메시지 목록

### 회원가입 관련
- "사용자명은 필수입니다"
- "사용자명은 3~20자여야 합니다"
- "사용자명은 영문과 숫자만 가능합니다"
- "비밀번호는 필수입니다"
- "비밀번호는 최소 8자 이상이어야 합니다"
- "이메일은 필수입니다"
- "올바른 이메일 형식이 아닙니다"
- "이미 존재하는 사용자명입니다"

### 로그인 관련
- "사용자명은 필수입니다"
- "비밀번호는 필수입니다"
- "잘못된 인증 정보"

### 일반 에러
- "유효성 검증 실패" (validation 에러 발생 시 기본 메시지)
- "잘못된 요청" (IllegalArgumentException 발생 시 기본 메시지)
