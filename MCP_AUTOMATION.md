# MCP 자동화 가이드

## 📋 개요

이 문서는 **Model Context Protocol (MCP)**를 활용한 테스트 자동화 방법을 설명합니다.
File MCP와 Browser MCP를 사용하여 테스트 케이스를 읽고, 브라우저를 제어하며, 결과를 엑셀로 생성하는 전체 프로세스를 다룹니다.

## 🎯 MCP란?

**Model Context Protocol (MCP)**는 AI가 외부 도구와 상호작용할 수 있게 해주는 프로토콜입니다.

### 사용하는 MCP 서버

1. **File MCP** (`@modelcontextprotocol/server-filesystem`)
   - 파일 읽기/쓰기
   - CSV, 텍스트 파일 처리
   - 허용된 디렉토리 내에서만 동작

2. **Browser Extension MCP** (`@executeautomation/mcp-playwright-server`)
   - 이미 열려있는 Chrome/Edge 브라우저 제어
   - 폼 입력, 버튼 클릭, 페이지 스냅샷
   - 실시간 시각적 확인 가능

## 🛠 MCP 설정

### MCP 서버 설정 파일

Cursor IDE의 MCP 설정은 다음 위치에 있습니다:
```
~/.cursor/projects/Users-mz02-horang-cdrive-login-backend/mcp-servers/package.json
```

### 설정 예시

```json
{
  "mcpServers": {
    "filesystem": {
      "command": "npx",
      "args": [
        "-y",
        "@modelcontextprotocol/server-filesystem",
        "/Users/mz02-horang/cdrive/login-backend"
      ]
    },
    "cursor-browser-extension": {
      "command": "npx",
      "args": [
        "-y",
        "@executeautomation/mcp-playwright-server"
      ]
    }
  }
}
```

### 허용된 디렉토리

File MCP는 다음 디렉토리 내에서만 파일에 접근 가능:
- `/Users/mz02-horang/cdrive/login-backend`

## 📁 자동화 도구

### 메인 도구: `project-tester` (Custom MCP Server)

**파이썬 스크립트를 대체하여 사용하는 프로젝트 전용 테스트 도구입니다.**

이 도구는:
- `/Users/mz02-horang/cdrive/login-backend/mcp-servers/index.js`에 구현됨
- CSV 읽기, API 호출, 결과 분석을 하나의 도구(`run_auth_test_suite`)로 처리
- Cursor IDE 내에서 직접 호출 가능

```javascript
// mcp.json 설정 예시
"project-tester": {
  "command": "node",
  "args": ["/Users/mz02-horang/cdrive/login-backend/mcp-servers/index.js"]
}
```

## 🔄 자동화 워크플로우

### 전체 프로세스

```
1. File MCP
   └─> test-cases-input.csv 읽기
        └─> 10개 테스트 케이스 파싱

2. API 테스트
   ├─> 회원가입 API (6개)
   └─> 로그인 API (3개 + 1개 실패 케이스)

3. 결과 수집
   ├─> HTTP 상태 코드
   ├─> 응답 메시지 (한국어)
   └─> 성공/실패 판정

4. 엑셀 생성
   ├─> 한국어 컬럼명
   ├─> 색상 포맷팅 (성공=녹색, 실패=빨간색)
   └─> test_results_korean_YYYYMMDD_HHMMSS.xlsx
```

## 📝 File MCP 활용

### CSV 파일 읽기

```python
# File MCP를 통해 원본 파일 직접 읽기
with open('/Users/mz02-horang/cdrive/test-cases.csv', 'r', encoding='utf-8') as f:
    reader = csv.DictReader(f)
    test_cases = list(reader)

# 결과
# [
#   {'TestType': 'REGISTER', 'Username': 'testuser01', ...},
#   {'TestType': 'REGISTER', 'Username': 'abc', ...},
#   ...
# ]
```

**파일 위치**:
- 입력: `/Users/mz02-horang/cdrive/test-cases.csv` (원본)
- 출력: `/Users/mz02-horang/cdrive/test_results_korean_*.xlsx` (결과)

### 파일 접근 제한

File MCP는 허용된 디렉토리 내에서만 파일 접근이 가능하지만, 
현재 설정으로는 `/Users/mz02-horang/cdrive/` 디렉토리의 파일들에 접근 가능합니다.

✅ **허용** (원본 파일):
```python
open('/Users/mz02-horang/cdrive/test-cases.csv')  # 입력
open('/Users/mz02-horang/cdrive/test_results_korean_*.xlsx')  # 출력
```

✅ **허용** (백엔드 프로젝트 내):
```python
open('/Users/mz02-horang/cdrive/login-backend/browser_full_test_korean_v2.py')
```

❌ **차단** (다른 사용자 디렉토리):
```python
open('/Users/other-user/data.csv')  # 접근 불가
```

## 🌐 Browser MCP 활용

### Browser Extension MCP 특징

1. **이미 열려있는 브라우저 제어**
   - 새 브라우저를 실행하지 않음
   - 사용자가 수동으로 Chrome/Edge 실행 필요

2. **시각적 확인 가능**
   - 브라우저 창에서 실시간으로 동작 확인
   - 디버깅 용이

3. **주요 기능**
   - 페이지 이동 (`browser_navigate`)
   - 폼 입력 (`browser_type`)
   - 버튼 클릭 (`browser_click`)
   - 페이지 스냅샷 (`browser_snapshot`)
   - 대기 (`browser_wait_for`)

### Browser MCP 사용 예시

#### 1. 페이지 이동
```javascript
await mcp_cursor-browser-extension_browser_navigate({
  url: "http://localhost:5173/register"
});
```

#### 2. 폼 입력 (천천히 타이핑)
```javascript
await mcp_cursor-browser-extension_browser_type({
  element: "사용자명 입력란",
  ref: "e10",  // 스냅샷에서 확인한 참조
  text: "testuser01",
  slowly: true  // 한 글자씩 천천히 입력
});
```

#### 3. 버튼 클릭
```javascript
await mcp_cursor-browser-extension_browser_click({
  element: "회원가입 버튼",
  ref: "e20"
});
```

#### 4. 결과 대기
```javascript
await mcp_cursor-browser-extension_browser_wait_for({
  time: 3  // 3초 대기
});
```

### Browser MCP 사용 시 주의사항

1. **브라우저 수동 실행 필요**
```bash
# Chrome 또는 Edge 실행 후
# http://localhost:5173 탭 열기
```

2. **브라우저 포커스 유지**
   - 테스트 실행 중 다른 작업 금지
   - 브라우저 창을 최소화하지 않기

3. **요소 참조 (ref) 확인**
   - 먼저 `browser_snapshot`으로 페이지 구조 파악
   - 정확한 `ref` 값 사용

## 📊 엑셀 리포트 생성

### 엑셀 구조

```python
headers = ['번호', '테스트유형', '사용자명', '이메일', '비밀번호', '설명', '예상결과', '실제결과', '판정', '실패사유']

# 헤더 스타일
header_fill = PatternFill(start_color="4472C4", end_color="4472C4", fill_type="solid")
header_font = Font(bold=True, color="FFFFFF", size=12)

# 판정 색상
if 'PASS' in value:
    cell.fill = PatternFill(start_color="C6EFCE", end_color="C6EFCE", fill_type="solid")  # 녹색
else:
    cell.fill = PatternFill(start_color="FFC7CE", end_color="FFC7CE", fill_type="solid")  # 빨간색

# 실패사유 색상
if value:
    cell.fill = PatternFill(start_color="FFF2CC", end_color="FFF2CC", fill_type="solid")  # 노란색
```

### 엑셀 컬럼 상세

| 컬럼명 | 너비 | 정렬 | 설명 |
|--------|------|------|------|
| 번호 | 8 | 중앙 | 테스트 순번 |
| 테스트유형 | 12 | 중앙 | 회원가입/로그인 |
| 사용자명 | 22 | 중앙 | 입력한 username |
| 이메일 | 25 | 좌측 | 입력한 email (로그인은 '-') |
| 비밀번호 | 18 | 좌측 | 입력한 password |
| 설명 | 35 | 좌측 | 테스트 설명 |
| 예상결과 | 10 | 좌측 | 성공/실패 |
| 실제결과 | 10 | 좌측 | 성공/실패 |
| 판정 | 12 | 좌측 | ✅ PASS / ❌ FAIL |
| 실패사유 | 50 | 좌측 | 한국어 에러 메시지 |

## 🔍 한국어 에러 메시지 처리

### 백엔드 에러 메시지 파싱

```python
if resp.status_code != 200:
    try:
        error_data = resp.json()
        
        # 메시지 추출
        if 'message' in error_data:
            fail_reason = error_data['message']  # 예: "유효성 검증 실패"
            
            # 상세 에러 추가
            if 'errors' in error_data and error_data['errors']:
                details = error_data['errors']
                if isinstance(details, dict):
                    # 예: {'username': '사용자명은 영문과 숫자만 가능합니다'}
                    fail_reason += f" (상세: {', '.join([f'{k}={v}' for k, v in details.items()])})"
    except:
        fail_reason = f'HTTP {resp.status_code}'
```

### 에러 메시지 예시

#### 유효성 검증 실패
```json
{
  "message": "유효성 검증 실패",
  "errors": {
    "username": "사용자명은 영문과 숫자만 가능합니다"
  }
}
```

**엑셀 출력**:
```
유효성 검증 실패 (상세: username=사용자명은 영문과 숫자만 가능합니다)
```

#### 인증 실패
```json
{
  "message": "잘못된 인증 정보"
}
```

**엑셀 출력**:
```
[예상: 성공, 실제: 실패] 잘못된 인증 정보
```

#### 중복 사용자명
```json
{
  "message": "이미 존재하는 사용자명입니다"
}
```

**엑셀 출력**:
```
이미 존재하는 사용자명입니다
```

## 🚀 실행 가이드

### 1. 사전 준비

```bash
# 1. 백엔드 실행
cd /Users/mz02-horang/cdrive/login-backend
./gradlew bootRun

# 2. 프론트엔드 실행
cd /Users/mz02-horang/cdrive/login-frontend
npm run dev

# 3. Python 라이브러리 설치
pip install openpyxl requests
```

### 2. 브라우저 준비

1. Chrome 또는 Edge 실행
2. `http://localhost:5173` 탭 열기
3. 브라우저 창을 그대로 유지

### 3. 테스트 실행

```bash
# 1. DB 초기화
mysql -u root -proot -e "USE login_backend; TRUNCATE TABLE users;"

# 2. AI에게 테스트 실행 명령
# 예: "프로젝트 테스터로 test-cases-input.csv 테스트 돌려줘"
```

**참고**: 더 이상 파이썬 스크립트를 수동으로 실행할 필요가 없습니다. AI가 `project-tester` MCP 도구를 사용하여 모든 과정을 자동으로 처리합니다.

### 4. 결과 확인

```bash
# 엑셀 파일 확인
ls -lt test_results_korean_*.xlsx | head -1

# 또는 Finder에서 열기
open test_results_korean_20260107_141007.xlsx
```

## 🐛 트러블슈팅

### 1. File MCP 에러: "Access denied"
**원인**: 파일이 허용 디렉토리 밖에 있음

**해결**:
```bash
# 파일을 허용 디렉토리로 복사
cp /Users/mz02-horang/cdrive/test-cases.csv \
   /Users/mz02-horang/cdrive/login-backend/test-cases-input.csv
```

### 2. Browser MCP 에러: "No browser connected"
**원인**: 브라우저가 열려있지 않음

**해결**:
- Chrome 또는 Edge 실행
- `http://localhost:5173` 탭 열기

### 3. 한국어 깨짐
**원인**: 엑셀 파일의 인코딩 문제

**해결**:
```python
# CSV 읽을 때 encoding 명시
with open('test-cases-input.csv', 'r', encoding='utf-8') as f:
    reader = csv.DictReader(f)
```

### 4. openpyxl 없음
**원인**: Python 라이브러리 미설치

**해결**:
```bash
pip install openpyxl
```

## 📚 참고 자료

### MCP 관련
- [MCP 공식 문서](https://modelcontextprotocol.io/)
- [File System MCP](https://github.com/modelcontextprotocol/servers/tree/main/src/filesystem)
- [Browser Extension MCP](https://github.com/executeautomation/mcp-playwright-server)

### Python 라이브러리
- [openpyxl 문서](https://openpyxl.readthedocs.io/)
- [requests 문서](https://requests.readthedocs.io/)

## 🔗 관련 문서

- [TESTING_GUIDE.md](TESTING_GUIDE.md) - 테스트 전반적인 가이드
- [API_SPECIFICATION.md](API_SPECIFICATION.md) - API 상세 스펙
- [README.md](README.md) - 프로젝트 개요

## 💡 AI 프롬프트 작성 팁

### MCP 자동화 테스트 추가 시

```
[참고 문서: MCP_AUTOMATION.md, TESTING_GUIDE.md]

{기능}에 대한 자동화 테스트를 추가해줘.

요구사항:
1. File MCP로 test-cases-input.csv에서 테스트 케이스 읽기
2. API 직접 호출하여 테스트 실행
3. 한국어 에러 메시지 파싱
4. 엑셀 리포트 생성 (한국어 컬럼명, 색상 포맷팅)

입력 데이터:
- 사용자명: {username}
- 이메일: {email}
- 비밀번호: {password}

예상 결과:
- {성공/실패}
- 에러 메시지: {메시지}
```

### MCP 관련 이슈 해결 시

```
[참고 문서: MCP_AUTOMATION.md - 트러블슈팅 섹션]

{에러 메시지}가 발생함.

현재 상황:
- 브라우저: {Chrome/Edge/없음}
- 파일 경로: {경로}
- 에러 로그: {로그}
```

