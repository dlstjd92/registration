package com.inspark.courseregistrationmodule.repository;

import com.inspark.courseregistrationmodule.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository  extends JpaRepository<Reservation, Long> {



}
