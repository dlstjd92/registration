package com.inspark.courseregistrationmodule.domain;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tutors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 이름

    @Column(nullable = false, unique = true)
    private String email;

    private String university; // 대학
    private String major; // 전공
    private String profileImageUrl; // 프로필이미지 링크(s3 사용 예정)

    @ElementCollection
    @CollectionTable(name = "tutor_core_tags", joinColumns = @JoinColumn(name = "tutor_id"))
    @Column(name = "tag")
    private List<String> coreTags; // 핵심 경험

    @ElementCollection
    @CollectionTable(name = "tutor_interests", joinColumns = @JoinColumn(name = "tutor_id"))
    @Column(name = "interest")
    private List<String> interests; // 관심사

    @Lob // Large object
    private String aboutMe; // 자기소개

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommendation_reason_id")
    private RecommendationReason recommendation; //추천 번호 매핑

}
