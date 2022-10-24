package dev.marcosalmeida.congestion.calcucator.repository;

import dev.marcosalmeida.congestion.calcucator.domain.TollFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

public interface TollFeeRepository extends JpaRepository<TollFee, UUID> {

    @Query("select f.amount from TollFee f where lower(f.city.name) = lower(:cityName) and :passTime between f.startTime and f.endTime")
    Optional<Integer> findFeeByCityAndTime(LocalTime passTime, String cityName);
}
