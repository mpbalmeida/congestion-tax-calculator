package dev.marcosalmeida.congestion.calcucator.repository;

import dev.marcosalmeida.congestion.calcucator.domain.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CityRepository extends JpaRepository<City, UUID> {
}
