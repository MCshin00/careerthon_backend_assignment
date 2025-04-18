# careerthon_backend_assignment
## 바로인턴 백엔드 개발 과제 (Java)
### 배포 주소 : http://15.164.20.131:8080

### Swagger UI 주소 : http://15.164.20.131:8080/swagger-ui/index.html

### 회원 가입 API 요청 주소(POST) : http://15.164.20.131:8080/signup
- request body
```json
{
	"username": "JIN HO",
	"password": "12341234",
	"nickname": "Mentos"
}
```

- response body
```json
{
    "userId": "4749a0b1-a719-4cfb-ba3f-c599a4db13c6",
    "username": "JIN HO",
    "nickname": "Mentos",
    "role": "USER"
}
```

### 로그인 API 요청 주소(POST) : http://15.164.20.131:8080/login
- request body
```json
{
	"username": "JIN HO",
	"password": "12341234"
}
```

- response body
```json
{
    "token": "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzIiwidXNlcm5hbWUiOiJKSU4gSE8iLCJyb2xlIjp7ImF1dGhvcml0eU5hbWUiOiJST0xFX1VTRVIifSwiZXhwIjoxNzMxMTU4OTM0LCJpYXQiOjE3MzExNTUzMzR9.cqYcF4zxTeEibhOOg91c_Mp8f3p18FEKpsb5Z7Brx44"
}
```

### 관리자 권한 부여 API 요청 주소(PATCH) : http://15.164.20.131:8080/15.164.20.131:8080/admin/users/{userId}/roles
- request body : 없음

- response body
```json
{
  "username": "JIN HO",
  "nickname": "Mentos",
  "role": "ADMIN"
}
```

### 실행 방법
- Gradle 빌드(./gradlew clean build)
- build/libs 폴더의 빌드된 파일 실행 (nohup java -jar assignment-0.0.1-SNAPSHOT.jar &)
- applcation.yml 파일의 jwt.secret.key 에 필요한 JWT_SECRET_KEY 환경 변수 추가 후 실행
- 관리자 권한 부여 테스트 시 (admin, 1234)로 로그인
