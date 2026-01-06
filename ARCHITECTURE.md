# Architecture Guidelines

이 문서는 Login Backend 프로젝트의 아키텍처 원칙과 코딩 가이드라인을 정의합니다.

## Layered Architecture

프로젝트는 계층형 아키텍처(Layered Architecture)를 따릅니다:

```
Controller Layer (API)
      ↓
Service Layer (Business Logic)
      ↓
Repository Layer (Data Access)
      ↓
Database (MySQL)
```

### 1. Controller Layer
- **위치**: `src/main/kotlin/com/example/loginbackend/controller/`
- **역할**: HTTP 요청/응답 처리, 라우팅
- **규칙**:
  - DTO 객체만 사용 (Entity 직접 노출 금지)
  - `@Valid` 애노테이션으로 입력 검증
  - 비즈니스 로직 포함하지 않음
  - 적절한 HTTP 상태 코드 반환

### 2. Service Layer
- **위치**: `src/main/kotlin/com/example/loginbackend/service/`
- **역할**: 비즈니스 로직 구현, 트랜잭션 관리
- **규칙**:
  - `@Service` 애노테이션 사용
  - `@Transactional` 애노테이션으로 트랜잭션 관리
  - Repository를 통해서만 데이터 접근
  - 예외 처리 및 검증 로직 포함

### 3. Repository Layer
- **위치**: `src/main/kotlin/com/example/loginbackend/repository/`
- **역할**: 데이터베이스 접근
- **규칙**:
  - Spring Data JPA 인터페이스 사용
  - 쿼리 메서드 명명 규칙 준수
  - Entity 객체만 다룸

### 4. Domain Layer
- **위치**: `src/main/kotlin/com/example/loginbackend/domain/`
- **역할**: JPA 엔티티 정의
- **규칙**:
  - `@Entity` 애노테이션 사용
  - 데이터베이스 테이블과 1:1 매핑
  - 컨트롤러에 직접 노출하지 않음

### 5. DTO Layer
- **위치**: `src/main/kotlin/com/example/loginbackend/dto/`
- **역할**: 데이터 전송 객체
- **규칙**:
  - Request DTO: 클라이언트 → 서버
  - Response DTO: 서버 → 클라이언트
  - Entity와 분리하여 민감 정보 보호
  - Validation 애노테이션 포함 가능

### 6. Configuration Layer
- **위치**: `src/main/kotlin/com/example/loginbackend/config/`
- **역할**: Spring Bean 설정, 애플리케이션 구성
- **규칙**:
  - `@Configuration` 애노테이션 사용
  - `@Bean` 메서드로 의존성 정의

---

## 코딩 규칙

### 네이밍 컨벤션
- **클래스**: PascalCase (예: `AuthController`, `UserService`)
- **함수/변수**: camelCase (예: `findUser`, `userName`)
- **상수**: UPPER_SNAKE_CASE (예: `MAX_LOGIN_ATTEMPTS`)
- **패키지**: lowercase (예: `controller`, `service`)

### 파일 구조
```
com.example.loginbackend
├── config/              # 설정 클래스
├── controller/          # REST API 컨트롤러
├── dto/                 # 데이터 전송 객체
├── domain/              # JPA 엔티티
├── repository/          # 데이터 저장소
├── service/             # 비즈니스 로직
└── LoginBackendApplication.kt  # 메인 클래스
```

### 보안 원칙
1. **비밀번호 암호화**: BCryptPasswordEncoder 사용
2. **민감정보 보호**: DTO를 통해 비밀번호 등 민감 정보 응답에서 제외
3. **입력 검증**: 모든 사용자 입력을 서버 측에서 검증
4. **SQL Injection 방지**: JPA/Hibernate 사용으로 자동 방지

### 예외 처리
- Service Layer에서 비즈니스 예외 발생
- Controller Layer에서 HTTP 응답으로 변환
- 의미 있는 에러 메시지 제공

---

## 개발 워크플로우

### 새 기능 추가 시
1. **API 스펙 정의**: `API_SPECIFICATION.md` 업데이트
2. **DTO 생성**: Request/Response DTO 작성
3. **Service 구현**: 비즈니스 로직 작성
4. **Controller 구현**: API 엔드포인트 추가
5. **테스트**: 수동/자동 테스트로 검증

### 코드 작성 순서
Entity → Repository → DTO → Service → Controller

---

## 참고 문서
- `project_structure.md`: 프로젝트 구조 및 기술 스택
- `API_SPECIFICATION.md`: API 엔드포인트 상세 스펙
- `application.yml`: 데이터베이스 및 JPA 설정
