package dev.marcosalmeida.congestion.calculator.repository;

import dev.marcosalmeida.congestion.calculator.domain.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CityRepository extends JpaRepository<City, UUID> {
}
