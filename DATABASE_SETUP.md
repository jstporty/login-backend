# 데이터베이스 설정 가이드

## MySQL 데이터베이스 설정

### 1. MySQL 접속
터미널에서 MySQL에 접속합니다. root 비밀번호를 입력하세요:
```bash
mysql -uroot -p
```

### 2. 데이터베이스 생성
MySQL 프롬프트에서 다음 명령어를 실행합니다:
```sql
CREATE DATABASE IF NOT EXISTS login_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. 데이터베이스 확인
```sql
SHOW DATABASES;
USE login_db;
```

### 4. MySQL 접속 종료
```sql
EXIT;
```

## application.yml 설정

`src/main/resources/application.yml` 파일에서 MySQL 비밀번호를 설정하세요:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/login_db?useSSL=false&allowPublicKeyRetrieval=true
    username: root
    password: YOUR_MYSQL_PASSWORD  # <<< 여기에 실제 MySQL root 비밀번호 입력
```

## 애플리케이션 실행

데이터베이스 설정 후 애플리케이션을 실행합니다:
```bash
./gradlew bootRun
```

애플리케이션이 시작되면 JPA가 자동으로 `users` 테이블을 생성합니다.

## 테이블 확인

애플리케이션 실행 후 MySQL에서 테이블이 생성되었는지 확인:
```sql
mysql -uroot -p
USE login_db;
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
| role     | varchar(255) | YES  |     | NULL    |                |
+----------+--------------+------+-----+---------+----------------+
```
