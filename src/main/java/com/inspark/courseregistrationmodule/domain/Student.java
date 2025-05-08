package com.inspark.courseregistrationmodule.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "students",
        indexes = { // 이메일 검색용 인덱스
                @Index(name = "idx_student_email", columnList = "email", unique = true)
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 학생 이름

    @Column(nullable = false, unique = true)
    private String email; // 이메일

    private String profileImageUrl; //프로필 이미지 <- 기본이미지 사용
}