package com.inspark.courseregistrationmodule.service;

import com.inspark.courseregistrationmodule.domain.Reservation;
import com.inspark.courseregistrationmodule.domain.Student;
import com.inspark.courseregistrationmodule.domain.Tutor;
import com.inspark.courseregistrationmodule.dto.ReservationRequestDto;
import com.inspark.courseregistrationmodule.dto.ReservationResponseDto;
import com.inspark.courseregistrationmodule.repository.ReservationRepository;
import com.inspark.courseregistrationmodule.repository.StudentRepository;
import com.inspark.courseregistrationmodule.repository.TutorAvailableTimeRepository;
import com.inspark.courseregistrationmodule.repository.TutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final TutorAvailableTimeRepository  tutorAvailableTimeRepository;

    private final StudentRepository studentRepository;

    private final ReservationRepository reservationRepository;

    private final TutorRepository tutorRepository;

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

    public List<ZonedDateTime> findStartTimesWithDurationAndClassLength(ZonedDateTime start, ZonedDateTime end, int classLength){

        if(start == null || end == null)
            throw new RuntimeException("Start and end cannot be null");

        // 수업길이는 30분 혹은 1시간. 추후 유연하게 조절가능함.
        if (classLength > MAX_CLASS_LENGTH || classLength < 1)
            throw new RuntimeException("Class length must be less or equal than " + MAX_CLASS_LENGTH);

        // 비어있는 시간을 쿼리 후
        List<ZonedDateTime> sortedAvailableTimes = tutorAvailableTimeRepository.findAvailableTimes(start, end);

        // 시간 블럭에 따른 가공
        return getContiguousTimes(classLength, sortedAvailableTimes);
    }

    private List<ZonedDateTime> getContiguousTimes(int classLength, List<ZonedDateTime> sortedAvailableTimes) {
        if (classLength <= 1) {
            return sortedAvailableTimes;
        }

        List<ZonedDateTime> validStartTimes = new ArrayList<>();
        for (int i = 0; i <= sortedAvailableTimes.size() - classLength; i++) {
            if (isContiguous(classLength, sortedAvailableTimes, i)) {
                validStartTimes.add(sortedAvailableTimes.get(i));
            }
        }

        return validStartTimes;
    }

    private boolean isContiguous(int classLength, List<ZonedDateTime> sortedAvailableTimes, int i) {
        long minuteDiff = Duration.between(sortedAvailableTimes.get(i + classLength - 1), sortedAvailableTimes.get(i)).toMinutes();
        boolean isContiguous = minuteDiff == TIME_BLOCK_MINUTES * (classLength - 1);
        return isContiguous;
    }

    public List<Tutor> findTutorWithTimeAndClassLength(ZonedDateTime time, int classLength){

        if (time == null)
            throw new RuntimeException("Time cannot be null");

        if (classLength > MAX_CLASS_LENGTH || classLength < 1)
            throw new RuntimeException("Class length must be less or equal than " + MAX_CLASS_LENGTH);

        // 시간블록 * 수업길이-1
        ZonedDateTime endClassTime = time.plusMinutes(TIME_BLOCK_MINUTES*(classLength-1));

        // 쿼리문 - 수업길이에 따른 빈 블록 찾아서 튜터 찾아오기
        return tutorAvailableTimeRepository.findTutorsWithTime(time, endClassTime, classLength);
    }

    // 예약하기
    @Transactional
    public void makeReservation(ReservationRequestDto dto) {
        Tutor tutor = tutorRepository.findById(dto.getTutorId())
                .orElseThrow(() -> new RuntimeException("튜터가 존재하지 않습니다."));
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new RuntimeException("학생이 존재하지 않습니다."));

        ZonedDateTime start = dto.getStartDate();
        int classLength = dto.getClassLength();

        if (classLength < 1 || classLength > MAX_CLASS_LENGTH) {
            throw new RuntimeException("ClassLength must be between 1 and " + MAX_CLASS_LENGTH);
        }

        checkTutorsAvailableTime(classLength, start, tutor);

        checkDuplicates(classLength, start, tutor);

        saveReservation(classLength, start, tutor, student);
    }

    private void checkTutorsAvailableTime(int classLength, ZonedDateTime start, Tutor tutor) {
        for (int i = 0; i < classLength; i++) {
            ZonedDateTime blockTime = start.plusMinutes(i * TIME_BLOCK_MINUTES);
            boolean isAvailable = tutorAvailableTimeRepository.existsByTutorIdAndAvailableTime(tutor.getId(), blockTime);
            if (!isAvailable) {
                throw new RuntimeException("튜터의 가능한 시간이 아닙니다: " + blockTime);
            }
        }
    }

    private void checkDuplicates(int classLength, ZonedDateTime start, Tutor tutor) {
        for (int i = 0; i < classLength; i++) {
            ZonedDateTime blockTime = start.plusMinutes(i * TIME_BLOCK_MINUTES);
            boolean exists = reservationRepository.existsByTutor_EmailAndStartDate(
                    tutor.getEmail(), blockTime);
            if (exists) {
                throw new RuntimeException("해당 시간(" + blockTime + ")에 이미 예약이 존재합니다.");
            }
        }
    }

    private void saveReservation(int classLength, ZonedDateTime start, Tutor tutor, Student student) {
        String lessonGroupId = start.toString() + "_" + tutor.getId();

        for (int i = 0; i < classLength; i++) {
            ZonedDateTime blockTime = start.plusMinutes(i * TIME_BLOCK_MINUTES);
            Reservation reservation = Reservation.builder()
                    .tutor(tutor)
                    .student(student)
                    .startDate(blockTime)
                    .lessonGroupId(lessonGroupId)
                    .classLength(classLength)
                    .build();
            reservationRepository.save(reservation);
        }
    }

    // 학생이 내가 예약한 수업 전부 보기
    public List<ReservationResponseDto> getReservationsByStudent(String email) {
        return reservationRepository.findAllByStudent_email(email);
    }

}
