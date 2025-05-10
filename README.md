# Course Registration Module

이 시스템은 튜터와 학생 간의 수업 예약을 관리하는 백엔드 시스템입니다.  
튜터는 수업 가능한 시간대를 설정하고, 학생은 해당 시간대를 선택해 수업을 예약할 수 있습니다.

---

## 1. 주요 기능

- 튜터 등록, 조회, 상세 조회  
- 튜터의 수업 가능 시간대 설정  
- 추천 메시지 기반 튜터 프로필 노출  
- 학생 등록 및 조회  
- 수업 예약 및 내역 조회  
- 하루 30분 단위로 구성된 타임슬롯 시스템  

---

## 2. 기술 스택

- Spring Boot, JPA (Hibernate), H2 (테스트 DB)  
- Lombok, Bean Validation (JSR-380), Springdoc (Swagger)  
- Thymeleaf (간단한 테스트용 프론트 UI)  

---

## 3. 도메인 설계

### Tutor

- 이름, 이메일, 전공, 자기소개 등의 튜터 프로필을 저장  
- `@ElementCollection`을 사용해 핵심 경험 및 관심사를 리스트 형태로 저장  
- 검색 기능은 필요 없으므로 N:M 구조 대신 컬렉션을 사용  
- 자기소개(`aboutMe`)는 `@Lob`을 통해 대용량 텍스트로 저장  

### Student

- 수업을 예약하는 학생 정보를 저장하는 도메인  
- 이름, 이메일, 학교, 프로필 이미지 등 기본 정보를 포함  
- `email`은 중복 방지를 위해 유니크 제약을 설정  
- 예약(`Reservation`) 엔티티와 1:N 관계를 가짐  

### TutorAvailableTime

- 튜터가 가능한 시간대를 요일 및 30분 단위 시간 슬롯(TimeBlock)으로 설정하는 도메인  
- `day_of_week + time_block_id + tutor_id` 복합 인덱스를 사용하여 예약 가능 시간 조회 시 성능 최적화  
- 튜터 기준으로 조회하는 용도의 인덱스도 추가  

---

## 4. 향후 확장 고려

- `coreTags`, `interests`가 검색 기준이 될 경우 → `Tag` 엔티티로 분리해 N:M 구조로 리팩토링 가능  
- 추천 메시지는 향후 Spring Cloud Config, Consul 등 외부 설정 서버로 분리하여 무배포 운영 가능  
- AWS S3 기반의 프로필 이미지 저장, 국제화(i18n), 관리자 UI 확장 등을 고려한 설계 구조  
