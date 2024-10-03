# TIARY

![Version](https://img.shields.io/badge/version-1.1.0--SNAPSHOT-red.svg)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=TIARY&metric=alert_status)](https://sonarcloud.io/project/overview?id=TIARY)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=TIARY&metric=coverage)](https://sonarcloud.io/project/overview?id=TIARY)

내가 오늘 배운 것을 작성하고 공유하는 플랫폼

## 환경 변수

TIARY를 실행하기 위해 다음과 같은 환경 변수를 설정해 주어야 합니다.

``` shell
# Amazon Web Services
AWS_ACCESS_KEY=<액세스 키>
AWS_SECRET_KEY=<비밀 액세스 키>
AWS_S3_BUCKET=<S3 버킷 이름>
AWS_STORAGE_URL=<이미지 저장소 URL>

# 데이터베이스
SPRING_DATASOURCE_URL_HOST=<호스트>
SPRING_DATASOURCE_URL_PORT=<포트>
SPRING_DATASOURCE_URL_DATABASE=<데이터베이스 이름>
SPRING_DATASOURCE_USERNAME=<유저 이름>
SPRING_DATASOURCE_PASSWORD=<비밀번호>

# JWT
JWT_ACCESS_TOKEN_SECRET_KEY=<액세스 토큰 비밀 키>
JWT_REFRESH_TOKEN_SECRET_KEY=<리프레시 토큰 비밀 키>

# 메일
SPRING_MAIL_USERNAME=<이메일>
SPRING_MAIL_PASSWORD=<비밀번호>

# OAuth
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_ID=<구글 OAuth 클라이언트 ID>
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_SECRET=<구글 OAuth 클라이언트 비밀>

# 시큐리티
SECURITY_CORS_ALLOWED_ORIGINS=<CORS 허용 URL>
```

## 배포

GitHub Actions를 이용하여 배포하기 위해 다음과 같은 Secrets를 설정해 주어야 합니다.

``` shell
# Amazon Web Services
AWS_ACCESS_KEY_ID=<액세스 키>
AWS_SECRET_ACCESS_KEY=<비밀 액세스 키>
AWS_REGION=<리전>

# Amazon ECS
AWS_ECS_CLUSTER=<클러스터 이름>
AWS_ECS_SERVICE=<서비스 이름>
AWS_ECS_TASK_DEFINITION=<작업 정의 이름>

# Amazon ECR
AWS_ECR_REPOSITORY=<리포지토리 이름>
AWS_ECR_IMAGE_TAG=<도커 이미지 태그>
DOCKERFILE=<도커 파일 경로>

# SonarCloud
SONAR_TOKEN=<토큰>
```

## 설계도

### Use Case 다이어그램

![Use Case 다이어그램](https://github.com/user-attachments/assets/6e607460-79ac-4df2-8ec2-5167087d5a95)

### ER 다이어그램

![ER 다이어그램](https://github.com/user-attachments/assets/8855b5a7-a55c-4c4b-b957-9478c99c815c)

### Class 다이어그램

![Class 다이어그램](https://github.com/user-attachments/assets/08ffe84b-429d-49d8-afff-32faaf5eaa21)

### Infrastructure 다이어그램

#### Development

![Infrastructure 다이어그램 (Development)](https://github.com/user-attachments/assets/099d1c5b-48c3-4b15-890c-a6296a07ca3f)

#### Production

![Infrastructure 다이어그램 (Production)](https://github.com/user-attachments/assets/718991a4-3921-4728-8ebf-4811801f73a6)

### 와이어프레임

![Wireframe](https://github.com/user-attachments/assets/bd2de1fe-4d28-4064-932d-e7a2f17ac52f)