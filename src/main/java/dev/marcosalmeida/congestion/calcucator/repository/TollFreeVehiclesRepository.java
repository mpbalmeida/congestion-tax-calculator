package dev.marcosalmeida.congestion.calcucator.repository;

import dev.marcosalmeida.congestion.calcucator.domain.TollFreeVehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface TollFreeVehiclesRepository extends JpaRepository<TollFreeVehicle, UUID> {

    @Query("select v from TollFreeVehicle v where lower(v.city.name) = lower(:cityName) and lower(v.vehicleType) = lower(:vehicleType)")
    Optional<TollFreeVehicle> findByCityAndType(String vehicleType, String cityName);
}
