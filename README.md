# Login Backend 프로젝트

## 📋 프로젝트 개요

Spring Boot + Kotlin으로 구현된 사용자 인증 백엔드 API 서버입니다.
회원가입, 로그인 기능을 제공하며, React 프론트엔드와 연동되어 동작합니다.

## 🎯 주요 기능

- ✅ 회원가입 (사용자명, 이메일, 비밀번호 검증)
- ✅ 로그인 (인증 정보 확인)
- ✅ 입력 유효성 검증 (한국어 에러 메시지)
- ✅ CORS 설정 (프론트엔드 연동)
- ✅ MySQL 데이터베이스 연동

## 🛠 기술 스택

### Backend
- **Language**: Kotlin 1.9
- **Framework**: Spring Boot 3.4.1
- **Build Tool**: Gradle (Kotlin DSL)
- **Database**: MySQL 8.0
- **ORM**: Spring Data JPA (Hibernate)

### 주요 의존성
- Spring Web (REST API)
- Spring Data JPA (데이터베이스 연동)
- MySQL Connector
- Jakarta Validation (입력 검증)

## 📁 프로젝트 구조

```
login-backend/
├── src/main/kotlin/com/example/loginbackend/
│   ├── controller/          # REST API 컨트롤러
│   │   └── AuthController.kt
│   ├── service/             # 비즈니스 로직
│   │   └── AuthService.kt
│   ├── repository/          # 데이터 접근 계층
│   │   └── UserRepository.kt
│   ├── domain/              # JPA 엔티티
│   │   └── User.kt
│   ├── dto/                 # 데이터 전송 객체
│   │   ├── SignupRequest.kt
│   │   ├── LoginRequest.kt
│   │   └── UserResponse.kt
│   ├── exception/           # 예외 처리
│   │   └── GlobalExceptionHandler.kt
│   └── config/              # 설정
│       └── WebConfig.kt (CORS)
├── src/main/resources/
│   └── application.yml      # 애플리케이션 설정
└── 문서/
    ├── API_SPECIFICATION.md      # API 명세서
    ├── ARCHITECTURE.md           # 아키텍처 가이드
    ├── DATABASE_SETUP.md         # DB 설정 가이드
    ├── TESTING_GUIDE.md          # 테스트 가이드
    └── MCP_AUTOMATION.md         # MCP 자동화 가이드
```

## 🚀 빠른 시작

### 1. 데이터베이스 설정
```bash
mysql -u root -p
CREATE DATABASE login_backend CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
EXIT;
```

자세한 내용은 [DATABASE_SETUP.md](DATABASE_SETUP.md) 참고

### 2. 애플리케이션 실행
```bash
./gradlew bootRun
```

서버가 `http://localhost:8080`에서 실행됩니다.

### 3. API 테스트
```bash
# 회원가입
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","email":"test@example.com","password":"password123"}'

# 로그인
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password123"}'
```

## 📖 문서 가이드

### 📚 **[DOCS_INDEX.md](DOCS_INDEX.md)** - 문서 전체 인덱스
**모든 문서의 목록과 상황별 참조 가이드**

### 💡 **[AI_PROMPT_GUIDE.md](AI_PROMPT_GUIDE.md)** - AI 프롬프트 작성 가이드
**AI에게 질문할 때 어떤 문서를 참조해야 하는지 안내**

---

### AI 프롬프트 작성 시 참고할 문서들

1. **[API_SPECIFICATION.md](API_SPECIFICATION.md)** 
   - API 엔드포인트 상세 스펙
   - Request/Response 형식
   - 한국어 에러 메시지 목록

2. **[ARCHITECTURE.md](ARCHITECTURE.md)**
   - 계층형 아키텍처 구조
   - 코딩 규칙 및 네이밍 컨벤션
   - 새 기능 추가 시 워크플로우

3. **[DATABASE_SETUP.md](DATABASE_SETUP.md)**
   - MySQL 데이터베이스 설정 방법
   - 테이블 구조 및 스키마
   - DB 초기화 및 관리

4. **[TESTING_GUIDE.md](TESTING_GUIDE.md)**
   - 테스트 케이스 작성 방법
   - Browser MCP를 활용한 자동화 테스트
   - 한국어 엑셀 리포트 생성

5. **[MCP_AUTOMATION.md](MCP_AUTOMATION.md)**
   - File MCP, Browser MCP 활용 방법
   - 자동화 테스트 스크립트 구조
   - 한국어 에러 메시지 처리

**💡 Tip**: 어떤 문서를 봐야 할지 모르겠다면 [AI_PROMPT_GUIDE.md](AI_PROMPT_GUIDE.md)를 먼저 확인하세요!

## 🔧 주요 설정

### Port
- Backend: `8080`
- Frontend: `5173` (Vite React)

### Database
- Host: `localhost:3306`
- Database: `login_backend`
- Username: `root`
- Password: `root` (개발 환경)

### CORS
- 모든 origin 허용 (`*`) - 개발 환경 전용
- 프로덕션에서는 특정 도메인으로 제한 필요

## 📝 API 엔드포인트

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/auth/register` | 회원가입 |
| POST | `/api/auth/login` | 로그인 |

자세한 내용은 [API_SPECIFICATION.md](API_SPECIFICATION.md) 참고

## 🧪 테스트

### 수동 테스트
- Postman, curl, 또는 프론트엔드 UI 사용

### 자동화 테스트 (MCP)
```bash
# DB 초기화
mysql -u root -proot -e "USE login_backend; TRUNCATE TABLE users;"

# 테스트 실행
python3 browser_full_test_korean_v2.py
```

결과는 `test_results_korean_YYYYMMDD_HHMMSS.xlsx` 파일로 생성됩니다.

자세한 내용은 [TESTING_GUIDE.md](TESTING_GUIDE.md) 참고

## 🤖 AI 프롬프트 작성 가이드

### 새 기능 추가 시
```
[참고 문서: API_SPECIFICATION.md, ARCHITECTURE.md]

{기능 설명}에 대한 API를 추가해줘.
- 계층형 아키텍처를 따라서 Entity → Repository → DTO → Service → Controller 순서로 작성
- 한국어 에러 메시지 사용
- 입력 유효성 검증 포함
```

### 버그 수정 시
```
[참고 문서: API_SPECIFICATION.md]

{문제 상황} 발생. {엔드포인트}에서 {에러 메시지}가 나옴.
예상 동작: {정상 동작 설명}
```

### 테스트 작성 시
```
[참고 문서: TESTING_GUIDE.md, MCP_AUTOMATION.md]

{기능}에 대한 자동화 테스트를 작성해줘.
- File MCP로 CSV 읽기
- Browser MCP로 UI 테스트
- 한국어 엑셀 리포트 생성
```

## 📚 참고 자료

- [Spring Boot 공식 문서](https://spring.io/projects/spring-boot)
- [Kotlin 공식 문서](https://kotlinlang.org/docs/home.html)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Jakarta Validation](https://jakarta.ee/specifications/bean-validation/)

## 🔗 관련 프로젝트

- **Frontend**: `/Users/mz02-horang/cdrive/login-frontend` (React + Vite)
- **테스트 케이스**: `test-cases-input.csv`

## 📞 문의

프로젝트 관련 문의 사항은 이슈를 등록해주세요.

