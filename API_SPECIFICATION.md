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
    "message": "Validation failed",
    "errors": {
      "username": "Username must be between 3 and 20 characters",
      "password": "Password must be at least 8 characters"
    }
  }
  ```
- HTTP 400 Bad Request - 중복 사용자명
  ```json
  {
    "message": "Username already exists"
  }
  ```

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
    "message": "Invalid credentials"
  }
  ```

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

1. **비밀번호 암호화**: BCrypt 해싱 알고리즘 사용
2. **비밀번호 응답 제외**: API 응답에 비밀번호 절대 포함하지 않음
3. **입력 검증**: Jakarta Validation 사용하여 서버 측 검증 수행
