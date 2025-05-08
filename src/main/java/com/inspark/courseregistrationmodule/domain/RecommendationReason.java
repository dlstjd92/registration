package com.inspark.courseregistrationmodule.domain;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recommendation_reasons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationReason {

    @Id
    private Integer id; // 추천번호

    @Column(nullable = false)
    private String message; // 추천사유 작성
}