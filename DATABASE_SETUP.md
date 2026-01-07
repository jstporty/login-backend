# 데이터베이스 설정 가이드

## 📋 개요

이 문서는 Login Backend 프로젝트의 MySQL 데이터베이스 설정 방법을 설명합니다.

## 🛠 MySQL 데이터베이스 설정

### 1. MySQL 접속
터미널에서 MySQL에 접속합니다. root 비밀번호를 입력하세요:
```bash
mysql -u root -p
```

### 2. 데이터베이스 생성
MySQL 프롬프트에서 다음 명령어를 실행합니다:
```sql
CREATE DATABASE IF NOT EXISTS login_backend CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

**주의**: 데이터베이스 이름은 `login_backend`입니다 (이전 `login_db`에서 변경됨).

### 3. 데이터베이스 확인
```sql
SHOW DATABASES;
USE login_backend;
```

### 4. MySQL 접속 종료
```sql
EXIT;
```

## ⚙️ application.yml 설정

`src/main/resources/application.yml` 파일 내용:

```yaml
spring:
  application:
    name: login-backend
  datasource:
    url: jdbc:mysql://localhost:3306/login_backend?useSSL=false&allowPublicKeyRetrieval=true
    username: root
    password: root  # 개발 환경 비밀번호
    driverClassName: com.mysql.cj.jdbc.Driver
  jpa:
    database-platform: org.hibernate.dialect.MySQL8Dialect
    hibernate:
      ddl-auto: update  # 테이블 자동 생성/업데이트
    show-sql: true  # SQL 쿼리 로그 출력
    properties:
      hibernate:
        format_sql: true  # SQL 포맷팅

server:
  port: 8080  # 백엔드 서버 포트
```

### 주요 설정 설명

- **ddl-auto: update**: 
  - 애플리케이션 실행 시 자동으로 테이블 생성/수정
  - 개발 환경에 적합
  - 프로덕션에서는 `validate` 또는 `none` 사용 권장

- **show-sql: true**: 
  - 실행되는 SQL 쿼리를 콘솔에 출력
  - 디버깅 및 학습에 유용

## 🚀 애플리케이션 실행

데이터베이스 설정 후 애플리케이션을 실행합니다:

### Gradle로 실행
```bash
cd /Users/mz02-horang/cdrive/login-backend
./gradlew bootRun
```

### 또는 JAR 파일로 실행
```bash
./gradlew build
java -jar build/libs/login-backend-0.0.1-SNAPSHOT.jar
```

애플리케이션이 시작되면 JPA가 자동으로 `users` 테이블을 생성합니다.

**실행 확인**:
```
Started LoginBackendApplication in X.XXX seconds
```

## 📊 테이블 확인

애플리케이션 실행 후 MySQL에서 테이블이 생성되었는지 확인:
```sql
mysql -u root -proot
USE login_backend;
SHOW TABLES;
DESCRIBE users;
```

예상되는 `users` 테이블 구조:
```
+----------+--------------+------+-----+---------+----------------+
| Field    | Type         | Null | Key | Default | Extra          |
+----------+--------------+------+-----+---------+----------------+
| id       | bigint       | NO   | PRI | NULL    | auto_increment |
| username | varchar(255) | NO   | UNI | NULL    |                |
| password | varchar(255) | NO   |     | NULL    |                |
| email    | varchar(255) | YES  |     | NULL    |                |
| role     | varchar(255) | YES  |     | USER    |                |
+----------+--------------+------+-----+---------+----------------+
```

### 컬럼 설명

- **id**: 기본 키 (Primary Key), 자동 증가
- **username**: 사용자명, 고유 (Unique), NOT NULL
- **password**: 비밀번호, NOT NULL (개발 환경에서는 평문 저장)
- **email**: 이메일, NULL 허용
- **role**: 사용자 역할, 기본값 "USER"

## 🗄️ 데이터베이스 초기화

### 테스트를 위한 데이터 삭제

테스트 실행 전 기존 데이터를 모두 삭제하려면:

```bash
mysql -u root -proot -e "USE login_backend; TRUNCATE TABLE users;"
```

또는 MySQL 프롬프트에서:
```sql
USE login_backend;
TRUNCATE TABLE users;
```

### 특정 사용자 삭제
```sql
USE login_backend;
DELETE FROM users WHERE username = 'testuser01';
```

### 모든 사용자 조회
```sql
USE login_backend;
SELECT * FROM users;
```

## 🔍 데이터베이스 관리

### 사용자 생성 확인
```sql
SELECT id, username, email, role FROM users ORDER BY id DESC LIMIT 5;
```

### 사용자 수 확인
```sql
SELECT COUNT(*) as user_count FROM users;
```

### 중복 사용자명 확인
```sql
SELECT username, COUNT(*) as count 
FROM users 
GROUP BY username 
HAVING count > 1;
```

## 🐛 트러블슈팅

### 1. "Access denied for user 'root'@'localhost'"
**원인**: 비밀번호가 틀림

**해결**:
- `application.yml`의 `password` 확인
- MySQL root 비밀번호 재설정:
```bash
mysql -u root -p
ALTER USER 'root'@'localhost' IDENTIFIED BY 'root';
```

### 2. "Unknown database 'login_backend'"
**원인**: 데이터베이스가 생성되지 않음

**해결**:
```bash
mysql -u root -proot -e "CREATE DATABASE login_backend CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
```

### 3. "Table 'users' doesn't exist"
**원인**: 애플리케이션이 실행되지 않아 테이블이 생성되지 않음

**해결**:
```bash
./gradlew bootRun
```

### 4. "Duplicate entry 'testuser' for key 'username'"
**원인**: 동일한 사용자명으로 회원가입 시도

**해결**:
```bash
# 기존 사용자 삭제
mysql -u root -proot -e "USE login_backend; DELETE FROM users WHERE username='testuser';"
```

## 🔐 보안 고려사항

### 개발 환경
- 비밀번호 평문 저장 (테스트 용이성)
- root 계정 사용
- 간단한 비밀번호 ("root")

### 프로덕션 환경 (권장)
- BCrypt로 비밀번호 암호화
- 전용 DB 사용자 생성 (root 사용 금지)
- 강력한 비밀번호 사용
- SSL/TLS 연결 사용
- `application.yml`을 환경 변수로 분리

**프로덕션 설정 예시**:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/login_backend?useSSL=true
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: validate  # 테이블 자동 생성 비활성화
```

## 📚 참고 자료

- [MySQL 공식 문서](https://dev.mysql.com/doc/)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Hibernate 공식 문서](https://hibernate.org/orm/documentation/)

## 🔗 관련 문서

- [README.md](README.md) - 프로젝트 개요
- [API_SPECIFICATION.md](API_SPECIFICATION.md) - API 상세 스펙
- [TESTING_GUIDE.md](TESTING_GUIDE.md) - 테스트 가이드
