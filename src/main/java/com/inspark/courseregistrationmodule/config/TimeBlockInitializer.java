package com.inspark.courseregistrationmodule.config;

import com.inspark.courseregistrationmodule.domain.TimeBlock;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class TimeBlockInitializer implements ApplicationRunner {
    // 스프링 서버 구동시, 미리 48개의 타임블록 생성하여 db 테이블 채우기

    private final EntityManager em;

    @Override
    public void run(ApplicationArguments args) {
        Long count = em.createQuery("SELECT COUNT(t) FROM TimeBlock t", Long.class).getSingleResult();
        if (count != 0) return;

        for (int i = 0; i < 48; i++) {
            String start = String.format("%02d:%02d", i / 2, (i % 2) * 30);
            String end = String.format("%02d:%02d", (i + 1) / 2, ((i + 1) % 2) * 30);
            TimeBlock block = TimeBlock.builder()
                    .startTime(start)
                    .endTime(end)
                    .label(start)
                    .build();
            em.persist(block);
        }
    }
}