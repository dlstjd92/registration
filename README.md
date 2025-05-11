# 수업 예약 시스템 - README

## 프로젝트 개요

이 프로젝트는 Spring Boot 기반의 학생-튜터 수업 예약 시스템입니다. 학생은 가능한 시간대를 조회하고 수업을 예약할 수 있으며, 튜터는 자신의 가능 시간을 관리할 수 있습니다.

## 주요 기능

### 학생 기능

* 학생 등록
* 튜터의 가능 시간과 수업 길이에 따라 가능한 수업 시작 시간 조회
* 수업 예약 (30분 또는 60분 수업 지원)
* 수업 그룹별로 내가 예약한 모든 수업 조회

### 튜터 기능

* 튜터 등록
* 가능 시간 블록 추가 및 삭제 (ZonedDateTime 포맷 사용)
* 중복되거나 잘못된 시간 블록 유효성 검사

### 예약 로직

* 예약은 30분 단위 블록으로 저장됨
* 하나의 수업에 여러 예약 블록이 연결되며 lessonGroupId 로 그룹핑됨
* 중복 예약 방지 및 연속된 블록을 가진 튜터만 필터링하여 제공

## 사용 기술

* Java 23
* Spring Boot
* Spring Data JPA (H2)
* Lombok
* Mockito + JUnit 5 (단위 테스트)

## 핵심 엔티티

### Student

* 필드: id, name, email, profileImageUrl

### Tutor

* 필드: id, name, email, university, major, profileImageUrl

### TutorAvailableTime

* 필드: id, tutor (외래키), availableTime (ZonedDateTime)

### Reservation

* 필드: id, tutor (외래키), student (외래키), startDate, lessonGroupId, classLength

## API 요약

### /student

#### POST /student/add : 학생 등록

```json
{
  "name": "학생1",
  "email": "student@example.com",
  "profileImageUrl": "http://image.url"
}
```

#### GET /student/findTime

```http
/student/findTime?start=2025-05-10T09:00:00Z&end=2025-05-10T12:00:00Z&classLength=2
```

기간 & 수업 길이로 현재 수업 가능한 시간대를 조회

#### GET /student/findTutor

```http
/student/findTutor?time=2025-05-10T10:00:00Z&classLength=2
```

시간대 & 수업길이로 수업 가능한 튜터 조회

#### POST /student/reservation : 수업 예약

```json
{
  "studentEmail": "student@example.com",
  "tutorEmail": "tutor@example.com",
  "startDate": "2025-05-10T10:00:00Z",
  "classLength": 2
}
```
시간대, 수업길이, 튜터로 새로운 수업 신청

#### GET /student/myLesson

```http
/student/myLesson?email=student@example.com
```

학생이 예약한 수업 목록 조회

### /tutor

#### POST /tutor/add : 튜터 등록

```json
{
  "name": "튜터1",
  "email": "tutor@example.com",
  "university": "학교",
  "major": "전공",
  "profileImageUrl": "http://image.url"
}
```

#### POST /tutor/addAvailableTime : 튜터 가능 시간 추가

```json
{
  "tutorEmail": "tutor@example.com",
  "times": ["2025-05-10T10:00:00Z", "2025-05-10T10:30:00Z"]
}
```

#### POST /tutor/removeAvailableTime : 튜터 가능 시간 제거

```json
{
  "tutorEmail": "tutor@example.com",
  "times": ["2025-05-10T10:00:00Z"]
}
```

## 테스트

### 단위 테스트

* StudentServiceTest, TutorServiceTest에서 다음을 테스트함:

  * 정상 및 예외 케이스
  * 중복 시간, 잘못된 입력, 연속 시간 여부 검사

### 리포지토리 테스트

* TutorAvailableTimeRepositoryTest

  * findAvailableTimes(): 예약되지 않은 시간 필터링
  * findTutorsWithTime(): 연속된 시간 블록 수를 만족하는 튜터만 반환

## 실행 방법

서버 실행 스크립트
```bash
./gradlew bootRun
```
테스트 실행 스크립트
```bash
./gradlew test
# 테스트 후 리포트
open build/reports/tests/test/index.html  # macOS
```

## Swagger 기반 API 테스트 방법

본 프로젝트는 Swagger UI를 통해 API 명세를 시각적으로 확인하고 바로 테스트할 수 있도록 구성되어 있습니다.

아래 방법 혹은, AWS EC2를 이용하여 구동중인 서버가 있습니다.
http://13.125.238.177:8080/swagger-ui/index.html#/

* 스팟 인스턴스 사용하여 갑작스럽게 종료될 수 있습니다.

### 접근 방법

* 서버 실행 후 브라우저에서 다음 주소로 접속:

```
http://localhost:8080/swagger-ui/index.html
```

### 제공 기능

* 각 API의 요청/응답 구조 명세 확인
* 직접 `Try it out` 버튼을 눌러 API 요청 실행
* 응답 코드 및 결과 확인 (ex. 200 OK, 400 Bad Request 등)

### 사용 예시

1. `POST /student/add` 예시
   ```
   {
   "name":"이름",
   "email":"이메일@이메일",
   "profileImageUrl":"사진"
   }
   ```

2. GET /student/findTime - 가능한 수업 시간 조회
```
   /student/findTime?start=2025-05-10T09:00:00Z&end=2025-05-10T12:00:00Z&classLength=2
```
3. /student/findTutor - 가능한 튜터 조회
```
   /student/findTutor?time=2025-05-10T10:00:00Z&classLength=2
```
4. POST /student/reservation - 수업 예약

```
   {
   "studentEmail": "이메일@이메일",
   "tutorEmail": "이메일@이메일",
   "startDate": "2025-05-10T10:00:00Z",
   "classLength": 2
   }
```
classLength 1당 30분을 의미.

5. GET /student/myLesson - 내가 예약한 수업 조회
```
   /student/myLesson?email=student1@example.com
```
6. POST /tutor/add - 튜터 등록
```
{
"name": "이름",
"email": "이메일@이메일",
"university": "학교",
"major": "전공",
"profileImageUrl": "사진주소"
}
```
7. POST /tutor/addAvailableTime - 튜터 가능 시간 추가
```
{
"tutorEmail": "이메일@이메일",
"times": ["2025-05-10T10:00:00Z", "2025-05-10T10:30:00Z"]
}
```
8. POST /tutor/removeAvailableTime - 튜터 가능 시간 제거
```
{
"tutorEmail": "이메일@이메일",
"times": ["2025-05-10T10:00:00Z"]
}
```
---
1.  엔드포인트 클릭
2. `Try it out` 버튼 클릭
3. JSON 형식의 요청 본문 작성
4. `Execute` 버튼 클릭하여 요청 실행
5. 하단의 응답 결과(상태 코드, 응답 메시지 등) 확인


