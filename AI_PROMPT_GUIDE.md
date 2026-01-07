# AI 프롬프트 작성 가이드

## 📋 개요

이 문서는 AI에게 프롬프트를 작성할 때 어떤 문서를 참조해야 하는지 가이드를 제공합니다.
각 상황별로 필요한 문서를 빠르게 찾아 컨텍스트에 포함시킬 수 있습니다.

## 📚 문서 목록

### 1. **[README.md](README.md)** - 프로젝트 개요
**언제 참조**: 프로젝트 전반에 대한 이해가 필요할 때

**포함 내용**:
- 프로젝트 개요 및 주요 기능
- 기술 스택 및 의존성
- 프로젝트 구조
- 빠른 시작 가이드
- 모든 문서 링크

**AI 프롬프트 예시**:
```
[참고: README.md]
이 프로젝트의 전체 구조를 설명해줘.
```

---

### 2. **[API_SPECIFICATION.md](API_SPECIFICATION.md)** - API 명세서
**언제 참조**: API 엔드포인트 관련 작업 시

**포함 내용**:
- 회원가입 API 상세 스펙
- 로그인 API 상세 스펙
- Request/Response 형식
- 에러 코드 및 메시지 (한국어)
- 데이터 모델 정의
- 유효성 검증 규칙

**AI 프롬프트 예시**:
```
[참고: API_SPECIFICATION.md]

회원가입 API에서 사용자명 유효성 검증 규칙을 알려줘.
```

```
[참고: API_SPECIFICATION.md]

로그인 실패 시 어떤 에러 메시지를 반환해야 하지?
```

---

### 3. **[ARCHITECTURE.md](ARCHITECTURE.md)** - 아키텍처 가이드
**언제 참조**: 코드 구조, 설계 패턴, 새 기능 추가 시

**포함 내용**:
- 계층형 아키텍처 (Controller → Service → Repository)
- 각 레이어의 역할 및 규칙
- 네이밍 컨벤션
- 파일 구조
- 보안 원칙
- 새 기능 추가 워크플로우

**AI 프롬프트 예시**:
```
[참고: ARCHITECTURE.md]

새로운 API 엔드포인트를 추가하려고 해. 
어떤 순서로 코드를 작성해야 하지?
```

```
[참고: ARCHITECTURE.md]

Service Layer에서 예외 처리는 어떻게 해야 하지?
```

---

### 4. **[DATABASE_SETUP.md](DATABASE_SETUP.md)** - DB 설정 가이드
**언제 참조**: 데이터베이스 설정, 테이블 구조 관련 작업 시

**포함 내용**:
- MySQL 데이터베이스 설정 방법
- application.yml 설정 설명
- users 테이블 구조
- 데이터베이스 초기화 방법
- 트러블슈팅

**AI 프롬프트 예시**:
```
[참고: DATABASE_SETUP.md]

users 테이블에 새로운 컬럼을 추가하고 싶어. 
어떻게 해야 하지?
```

```
[참고: DATABASE_SETUP.md]

테스트 전에 데이터베이스를 초기화하는 명령어가 뭐야?
```

---

### 5. **[TESTING_GUIDE.md](TESTING_GUIDE.md)** - 테스트 가이드
**언제 참조**: 테스트 작성, 테스트 실행 관련 작업 시

**포함 내용**:
- 테스트 유형 (수동/자동화)
- 테스트 케이스 정의
- 자동화 테스트 실행 방법
- 엑셀 리포트 구조
- curl을 이용한 수동 테스트
- 문제 해결 방법

**AI 프롬프트 예시**:
```
[참고: TESTING_GUIDE.md]

회원가입 API를 curl로 테스트하고 싶어. 
명령어를 알려줘.
```

```
[참고: TESTING_GUIDE.md]

새로운 테스트 케이스를 추가하려면 어떻게 해야 하지?
```

---

### 6. **[MCP_AUTOMATION.md](MCP_AUTOMATION.md)** - MCP 자동화 가이드
**언제 참조**: MCP를 활용한 자동화 테스트 관련 작업 시

**포함 내용**:
- MCP 개요 및 설정
- File MCP 사용법
- Browser MCP 사용법
- 자동화 스크립트 구조
- 한국어 에러 메시지 처리
- 엑셀 리포트 생성
- 트러블슈팅

**AI 프롬프트 예시**:
```
[참고: MCP_AUTOMATION.md]

File MCP로 CSV 파일을 읽는 방법을 알려줘.
```

```
[참고: MCP_AUTOMATION.md]

Browser MCP가 "No browser connected" 에러를 내는데 
어떻게 해결하지?
```

---

## 🎯 상황별 문서 참조 가이드

### 새 기능 추가
```
[참고: ARCHITECTURE.md, API_SPECIFICATION.md]

{기능명} 기능을 추가하려고 해.

요구사항:
- {요구사항 1}
- {요구사항 2}

ARCHITECTURE.md의 워크플로우를 따라서 
Entity → Repository → DTO → Service → Controller 순서로 작성해줘.
```

### 버그 수정
```
[참고: API_SPECIFICATION.md]

{엔드포인트}에서 {문제 상황} 발생.

현재 응답:
{현재 응답}

예상 응답 (API_SPECIFICATION.md 기준):
{예상 응답}

문제를 찾아서 수정해줘.
```

### 테스트 작성
```
[참고: TESTING_GUIDE.md, MCP_AUTOMATION.md]

{기능}에 대한 자동화 테스트를 작성해줘.

요구사항:
- File MCP로 CSV 읽기
- API 호출하여 테스트
- 한국어 엑셀 리포트 생성

MCP_AUTOMATION.md의 구조를 따라서 작성해줘.
```

### 데이터베이스 변경
```
[참고: DATABASE_SETUP.md, ARCHITECTURE.md]

users 테이블에 {컬럼명} 컬럼을 추가하려고 해.

요구사항:
- 타입: {타입}
- NULL 허용: {예/아니오}
- 기본값: {기본값}

Entity 수정과 함께 관련 DTO도 업데이트해줘.
```

### 에러 메시지 수정
```
[참고: API_SPECIFICATION.md]

{에러 상황}에서 반환되는 에러 메시지를 수정하려고 해.

현재: {현재 메시지}
변경 후: {변경할 메시지}

API_SPECIFICATION.md의 에러 메시지 목록도 함께 업데이트해줘.
```

### MCP 트러블슈팅
```
[참고: MCP_AUTOMATION.md - 트러블슈팅 섹션]

{MCP 에러 메시지}가 발생했어.

현재 상황:
- 사용 중인 MCP: {File/Browser}
- 파일 경로/브라우저 상태: {정보}
- 에러 로그: {로그}

MCP_AUTOMATION.md의 트러블슈팅 섹션을 참고해서 해결 방법을 알려줘.
```

## 📖 문서 읽는 순서 (신규 개발자용)

### 1단계: 프로젝트 이해
1. **README.md** - 프로젝트 전체 개요
2. **ARCHITECTURE.md** - 코드 구조 및 설계 원칙

### 2단계: API 이해
3. **API_SPECIFICATION.md** - API 상세 스펙
4. **DATABASE_SETUP.md** - 데이터베이스 구조

### 3단계: 테스트 이해
5. **TESTING_GUIDE.md** - 테스트 방법
6. **MCP_AUTOMATION.md** - 자동화 테스트

## 🔍 문서 검색 팁

### 키워드로 찾기

| 찾고 싶은 내용 | 문서 | 키워드 |
|---------------|------|--------|
| API 엔드포인트 | API_SPECIFICATION.md | "POST", "Endpoint" |
| 에러 메시지 | API_SPECIFICATION.md | "Error", "message" |
| 계층 구조 | ARCHITECTURE.md | "Layer", "Controller" |
| 코딩 규칙 | ARCHITECTURE.md | "네이밍", "규칙" |
| 테이블 구조 | DATABASE_SETUP.md | "users", "테이블" |
| DB 초기화 | DATABASE_SETUP.md | "TRUNCATE", "초기화" |
| 테스트 실행 | TESTING_GUIDE.md | "실행", "python3" |
| MCP 설정 | MCP_AUTOMATION.md | "MCP 설정", "mcpServers" |
| 에러 해결 | 각 문서의 "트러블슈팅" 섹션 | "트러블슈팅", "문제" |

## 💡 효과적인 프롬프트 작성 팁

### 1. 명확한 참조 표시
```
[참고: API_SPECIFICATION.md, ARCHITECTURE.md]
```

### 2. 구체적인 요구사항
```
요구사항:
- 한국어 에러 메시지 사용
- ARCHITECTURE.md의 계층 구조 준수
- API_SPECIFICATION.md의 유효성 검증 규칙 적용
```

### 3. 예상 결과 명시
```
예상 결과:
- Entity 수정
- Repository 메서드 추가
- DTO 업데이트
- Service 로직 구현
- Controller 엔드포인트 추가
```

### 4. 관련 컨텍스트 제공
```
현재 상황:
- 파일: AuthService.kt
- 문제: {문제 설명}
- 에러 메시지: {에러}
```

## 🎓 실전 예제

### 예제 1: 새 API 추가
```
[참고: ARCHITECTURE.md, API_SPECIFICATION.md, DATABASE_SETUP.md]

사용자 프로필 조회 API를 추가하려고 해.

요구사항:
- 엔드포인트: GET /api/users/{id}
- 응답: UserResponse (id, username, email, role)
- 존재하지 않는 사용자: 404 에러
- 에러 메시지: "사용자를 찾을 수 없습니다"

ARCHITECTURE.md의 계층형 구조를 따라서:
1. Repository에 findById 메서드 (이미 있으면 스킵)
2. Service에 getUserById 로직
3. Controller에 GET 엔드포인트

완료 후 API_SPECIFICATION.md에 스펙 추가해줘.
```

### 예제 2: 테스트 자동화
```
[참고: TESTING_GUIDE.md, MCP_AUTOMATION.md]

프로필 조회 API 테스트를 자동화하려고 해.

테스트 케이스:
1. 존재하는 사용자 조회 → 200 OK
2. 존재하지 않는 사용자 조회 → 404 Not Found

요구사항:
- test-cases-input.csv에 케이스 추가
- browser_full_test_korean_v2.py 수정
- 한국어 엑셀 리포트에 결과 포함

MCP_AUTOMATION.md의 구조를 참고해서 스크립트 수정해줘.
```

### 예제 3: 성능 개선
```
[참고: ARCHITECTURE.md, DATABASE_SETUP.md]

사용자 목록 조회가 느려.

현재 상황:
- 사용자 수: 10,000명
- 조회 시간: 5초
- 쿼리: SELECT * FROM users

개선 방안:
1. 페이지네이션 추가
2. 인덱스 최적화
3. 불필요한 컬럼 제외

ARCHITECTURE.md의 Repository Layer 규칙을 따라서 구현해줘.
```

## 🔗 문서 간 관계도

```
README.md (프로젝트 진입점)
    ├── ARCHITECTURE.md (설계 및 구조)
    │   ├── Controller ──→ API_SPECIFICATION.md
    │   ├── Service
    │   ├── Repository ──→ DATABASE_SETUP.md
    │   └── Domain ──→ DATABASE_SETUP.md
    │
    ├── API_SPECIFICATION.md (API 스펙)
    │   ├── Request/Response
    │   └── 에러 메시지
    │
    ├── DATABASE_SETUP.md (DB 설정)
    │   ├── 테이블 구조
    │   └── 초기화 방법
    │
    └── 테스트
        ├── TESTING_GUIDE.md (테스트 방법)
        └── MCP_AUTOMATION.md (자동화)
```

## 📞 문의

문서 관련 문의나 개선 제안은 이슈로 등록해주세요.

