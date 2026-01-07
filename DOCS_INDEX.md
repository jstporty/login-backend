# 📚 문서 인덱스

## 프로젝트 문서 전체 목록

이 프로젝트는 체계적인 문서화를 통해 AI 프롬프트 작성 시 쉽게 참조할 수 있도록 구성되어 있습니다.

---

## 📋 문서 목록

### 🎯 **[README.md](README.md)** - 프로젝트 개요
**가장 먼저 읽어야 할 문서**

프로젝트의 전반적인 개요, 기술 스택, 빠른 시작 방법을 제공합니다.

**주요 내용**:
- 프로젝트 소개
- 주요 기능
- 기술 스택
- 프로젝트 구조
- 빠른 시작 가이드
- 모든 문서 링크

---

### 🏗️ **[ARCHITECTURE.md](ARCHITECTURE.md)** - 아키텍처 가이드
**코드 작성 전 반드시 읽어야 할 문서**

계층형 아키텍처, 코딩 규칙, 새 기능 추가 워크플로우를 설명합니다.

**주요 내용**:
- Layered Architecture (Controller → Service → Repository → Database)
- 각 레이어의 역할 및 규칙
- 네이밍 컨벤션 (PascalCase, camelCase)
- 파일 구조
- 보안 원칙
- 예외 처리 방법
- 개발 워크플로우

**AI 프롬프트 예시**:
```
[참고: ARCHITECTURE.md]
새 기능을 추가할 때 어떤 순서로 작성해야 하지?
```

---

### 🌐 **[API_SPECIFICATION.md](API_SPECIFICATION.md)** - API 명세서
**API 관련 작업 시 참조 문서**

모든 API 엔드포인트의 상세 스펙과 에러 메시지를 정의합니다.

**주요 내용**:
- 회원가입 API (`POST /api/auth/register`)
- 로그인 API (`POST /api/auth/login`)
- Request/Response 형식
- 유효성 검증 규칙 (한국어)
- 에러 메시지 목록 (한국어)
- Data Models

**AI 프롬프트 예시**:
```
[참고: API_SPECIFICATION.md]
회원가입 API에서 username 유효성 검증 규칙이 뭐야?
```

---

### 🗄️ **[DATABASE_SETUP.md](DATABASE_SETUP.md)** - DB 설정 가이드
**데이터베이스 설정 및 관리 문서**

MySQL 데이터베이스 설정, 테이블 구조, 초기화 방법을 설명합니다.

**주요 내용**:
- MySQL 설치 및 설정
- `login_backend` 데이터베이스 생성
- `application.yml` 설정 설명
- `users` 테이블 구조
- 데이터베이스 초기화 방법
- 트러블슈팅

**AI 프롬프트 예시**:
```
[참고: DATABASE_SETUP.md]
테스트 전에 DB를 초기화하는 명령어가 뭐야?
```

---

### 🧪 **[TESTING_GUIDE.md](TESTING_GUIDE.md)** - 테스트 가이드
**테스트 작성 및 실행 문서**

수동 테스트와 자동화 테스트 방법을 상세하게 설명합니다.

**주요 내용**:
- 테스트 유형 (수동/자동화)
- 테스트 케이스 정의 (`test-cases-input.csv`)
- 자동화 테스트 실행 방법
- 엑셀 리포트 구조 (한국어)
- curl을 이용한 수동 테스트
- 문제 해결 방법

**AI 프롬프트 예시**:
```
[참고: TESTING_GUIDE.md]
새로운 테스트 케이스를 추가하려면 어떻게 해야 하지?
```

---

### 🤖 **[MCP_AUTOMATION.md](MCP_AUTOMATION.md)** - MCP 자동화 가이드
**MCP를 활용한 자동화 테스트 심화 문서**

File MCP와 Browser MCP를 사용한 자동화 테스트의 모든 것을 다룹니다.

**주요 내용**:
- MCP (Model Context Protocol) 개요
- File MCP 활용 (CSV 읽기/쓰기)
- Browser MCP 활용 (브라우저 제어)
- 자동화 스크립트 구조 (`browser_full_test_korean_v2.py`)
- 한국어 에러 메시지 파싱
- 엑셀 리포트 생성 (색상 포맷팅)
- MCP 트러블슈팅

**AI 프롬프트 예시**:
```
[참고: MCP_AUTOMATION.md]
File MCP로 CSV를 읽는 코드를 작성해줘.
```

---

### 💡 **[AI_PROMPT_GUIDE.md](AI_PROMPT_GUIDE.md)** - AI 프롬프트 작성 가이드
**AI에게 질문할 때 참조할 문서 안내**

상황별로 어떤 문서를 참조해야 하는지 가이드를 제공합니다.

**주요 내용**:
- 각 문서의 용도 및 포함 내용
- 상황별 문서 참조 가이드
- 효과적인 프롬프트 작성 팁
- 실전 예제
- 문서 간 관계도

**AI 프롬프트 예시**:
```
[참고: AI_PROMPT_GUIDE.md]
새 API를 추가할 때 어떤 문서들을 참조해야 하지?
```

---

## 📊 문서 간 관계도

```
DOCS_INDEX.md (여기)
    │
    ├─── README.md ──────────────┐
    │    (프로젝트 진입점)          │
    │                            │
    ├─── AI_PROMPT_GUIDE.md      │
    │    (문서 참조 가이드)         │
    │                            │
    ├─── ARCHITECTURE.md         │
    │    (설계 및 구조)            ├──→ 코드 작성
    │                            │
    ├─── API_SPECIFICATION.md    │
    │    (API 스펙)              ├──→ API 개발
    │                            │
    ├─── DATABASE_SETUP.md       │
    │    (DB 설정)               ├──→ 데이터베이스
    │                            │
    ├─── TESTING_GUIDE.md        │
    │    (테스트 방법)            ├──→ 테스트
    │                            │
    └─── MCP_AUTOMATION.md       │
         (자동화 테스트)           └──→ 자동화
```

---

## 🎯 상황별 문서 가이드

### 💻 코드 작성
1. **ARCHITECTURE.md** - 구조 및 규칙 확인
2. **API_SPECIFICATION.md** - API 스펙 확인
3. **DATABASE_SETUP.md** - DB 구조 확인

### 🐛 버그 수정
1. **API_SPECIFICATION.md** - 예상 동작 확인
2. **ARCHITECTURE.md** - 코드 구조 확인

### 🧪 테스트 작성
1. **TESTING_GUIDE.md** - 테스트 방법 확인
2. **MCP_AUTOMATION.md** - 자동화 방법 확인

### 🗄️ DB 작업
1. **DATABASE_SETUP.md** - DB 설정 및 구조
2. **ARCHITECTURE.md** - Entity/Repository 규칙

### 🤖 AI 프롬프트
1. **AI_PROMPT_GUIDE.md** - 프롬프트 작성 가이드
2. 상황에 맞는 문서 선택

---

## 📖 읽는 순서 (신규 개발자용)

### 1일차: 프로젝트 이해
1. ✅ **README.md** (15분)
2. ✅ **ARCHITECTURE.md** (30분)
3. ✅ **DATABASE_SETUP.md** (20분)

### 2일차: API 및 테스트 이해
4. ✅ **API_SPECIFICATION.md** (30분)
5. ✅ **TESTING_GUIDE.md** (40분)

### 3일차: 자동화 이해
6. ✅ **MCP_AUTOMATION.md** (50분)
7. ✅ **AI_PROMPT_GUIDE.md** (30분)

**총 학습 시간**: 약 3시간 30분

---

## 🔍 문서 검색 치트시트

| 찾고 싶은 내용 | 문서 | 키워드 |
|---------------|------|--------|
| 프로젝트 개요 | README.md | "개요", "기술 스택" |
| 계층 구조 | ARCHITECTURE.md | "Layer", "Controller" |
| 코딩 규칙 | ARCHITECTURE.md | "네이밍", "규칙" |
| API 엔드포인트 | API_SPECIFICATION.md | "POST", "Endpoint" |
| 에러 메시지 | API_SPECIFICATION.md | "에러", "message" |
| DB 설정 | DATABASE_SETUP.md | "MySQL", "설정" |
| 테이블 구조 | DATABASE_SETUP.md | "users", "테이블" |
| 테스트 실행 | TESTING_GUIDE.md | "실행", "python3" |
| MCP 설정 | MCP_AUTOMATION.md | "MCP", "설정" |
| 프롬프트 작성 | AI_PROMPT_GUIDE.md | "프롬프트", "참조" |

---

## 📝 문서 업데이트 규칙

### 새 기능 추가 시
1. **API_SPECIFICATION.md** 업데이트 (API 추가 시)
2. **TESTING_GUIDE.md** 업데이트 (테스트 케이스 추가)
3. **README.md** 업데이트 (주요 기능 변경 시)

### 구조 변경 시
1. **ARCHITECTURE.md** 업데이트 (레이어 변경 시)
2. **DATABASE_SETUP.md** 업데이트 (테이블 변경 시)

### 자동화 변경 시
1. **MCP_AUTOMATION.md** 업데이트 (스크립트 변경 시)
2. **TESTING_GUIDE.md** 업데이트 (테스트 프로세스 변경 시)

---

## 💡 효과적인 문서 활용 팁

### 1. AI 프롬프트에 문서 명시
```
[참고: ARCHITECTURE.md, API_SPECIFICATION.md]
```

### 2. 여러 문서 동시 참조
```
[참고: ARCHITECTURE.md - 계층 구조, API_SPECIFICATION.md - 에러 메시지]
```

### 3. 구체적인 섹션 참조
```
[참고: MCP_AUTOMATION.md - 트러블슈팅 섹션]
```

---

## 🚀 빠른 시작

### 처음 시작하는 경우
1. **[README.md](README.md)** 읽기
2. DB 설정: **[DATABASE_SETUP.md](DATABASE_SETUP.md)** 따라하기
3. 애플리케이션 실행

### 개발하는 경우
1. **[ARCHITECTURE.md](ARCHITECTURE.md)** 읽고 구조 이해
2. **[API_SPECIFICATION.md](API_SPECIFICATION.md)** 참고하며 개발

### 테스트하는 경우
1. **[TESTING_GUIDE.md](TESTING_GUIDE.md)** 읽고 테스트 실행
2. **[MCP_AUTOMATION.md](MCP_AUTOMATION.md)** 참고하며 자동화

### AI에게 질문하는 경우
1. **[AI_PROMPT_GUIDE.md](AI_PROMPT_GUIDE.md)** 읽고 적절한 문서 선택
2. 문서를 명시하여 프롬프트 작성

---

## 📞 문의

문서 관련 문의나 개선 제안은 이슈로 등록해주세요.

**마지막 업데이트**: 2026년 1월 7일
