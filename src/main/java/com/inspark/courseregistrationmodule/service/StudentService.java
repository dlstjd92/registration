package com.inspark.courseregistrationmodule.service;


import com.inspark.courseregistrationmodule.domain.Student;
import com.inspark.courseregistrationmodule.repository.ReservationRepository;
import com.inspark.courseregistrationmodule.repository.StudentRepository;
import com.inspark.courseregistrationmodule.repository.TutorAvailableTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final TutorAvailableTimeRepository tutorAvailableTimeRepository;

    private final StudentRepository studentRepository;

    private final ReservationRepository reservationRepository;

    // 최대 수업길이는 1시간. 1 = 30분
    public static final int MAX_CLASS_LENGTH = 2;
    private static final int TIME_BLOCK_MINUTES = 30;

    public void addStudent(Student student){
        if (student == null)
            throw new RuntimeException("Student cannot be null");

        boolean exists = studentRepository.existsByEmail(student.getEmail());
        if (exists)
            throw new RuntimeException("Email already exists");

        studentRepository.save(student);
    }

    public List<ZonedDateTime> findTimeWithDurationAndClassLength(ZonedDateTime start, ZonedDateTime end, int classLength){

        if(start == null || end == null)
            throw new RuntimeException("Start and end cannot be null");

        // 수업길이는 30분 혹은 1시간. 추후 유연하게 조절가능함.
        if (classLength > MAX_CLASS_LENGTH)
            throw new RuntimeException("Class length must be less than " + MAX_CLASS_LENGTH);

        // 비어있는 시간을 쿼리 후
        List<ZonedDateTime> availableTime = tutorAvailableTimeRepository.findAvailableTime(start, end);

        // 시간 블럭에 따른 가공
        List<ZonedDateTime> result = new ArrayList<>();

        if (classLength > 1) { // 만약 시간이 1보다 커 블록별 탐색을 해야할 경우
            ZonedDateTime base = availableTime.getFirst(); // 기준시간
            List<Long> prefixTime = new ArrayList<>(); // 기준시간으로 프리픽스 섬

            for (ZonedDateTime time : availableTime) {
                long minutes = Duration.between(base, time).toMinutes();
                prefixTime.add(minutes);
            }
            // 시간 기준으로 단위만큼 점프해 의도한 시간과 맞는지 확인
            for (int i = 0; i <= prefixTime.size() - classLength; i++) {
                if (prefixTime.get(i + classLength - 1) - prefixTime.get(i) == TIME_BLOCK_MINUTES * (classLength - 1)) {
                    result.add(availableTime.get(i));
                }
            }
        } else { // 1이면 가공필요 없음
            result = availableTime;
        }

        return result;
    }

    // 시간, 수업길이, 튜터으로 학생이 수강신청
    // 수업길이를 그냥 시작시간 끝시간으로 표시? or 특정 데이터타입으로 표시하기 <- 가능여부를 알면 사실 고민거리가 아님
//    public void addLesson(ZonedDateTime dateTime,) {


//    }

}
