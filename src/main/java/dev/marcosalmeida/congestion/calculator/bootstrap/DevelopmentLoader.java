package dev.marcosalmeida.congestion.calculator.bootstrap;

import dev.marcosalmeida.congestion.calculator.domain.City;
import dev.marcosalmeida.congestion.calculator.domain.TollFee;
import dev.marcosalmeida.congestion.calculator.domain.TollFreeVehicle;
import dev.marcosalmeida.congestion.calculator.repository.CityRepository;
import dev.marcosalmeida.congestion.calculator.repository.TollFeeRepository;
import dev.marcosalmeida.congestion.calculator.repository.TollFreeVehiclesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
@Component
public class DevelopmentLoader implements CommandLineRunner {

    private final CityRepository cityRepository;
    private final TollFreeVehiclesRepository tollFreeVehiclesRepository;

    private final TollFeeRepository tollFeeRepository;


    @Override
    public void run(String... args) throws Exception {
        createCities();
    }

    private void createCities() {
        var gothenburg = City.builder()
                .name("Gothenburg")
                .build();

        var stockholm = City.builder()
                .name("Stockholm")
                .build();

        cityRepository.saveAll(List.of(gothenburg, stockholm));

        var toolFreeGot1 = TollFreeVehicle.builder().vehicleType("motorcycle").city(gothenburg).build();
        var toolFreeGot2 = TollFreeVehicle.builder().vehicleType("tractor").city(gothenburg).build();
        var toolFreeGot3 = TollFreeVehicle.builder().vehicleType("emergency").city(gothenburg).build();
        var toolFreeGot4 = TollFreeVehicle.builder().vehicleType("diplomat").city(gothenburg).build();
        var toolFreeGot5 = TollFreeVehicle.builder().vehicleType("foreign").city(gothenburg).build();
        var toolFreeGot6 = TollFreeVehicle.builder().vehicleType("military").city(gothenburg).build();

        tollFreeVehiclesRepository.saveAll(
                List.of(
                        toolFreeGot1,
                        toolFreeGot2,
                        toolFreeGot3,
                        toolFreeGot4,
                        toolFreeGot5,
                        toolFreeGot6
                )
        );

        var tollFreeTax1 = TollFee.builder().city(gothenburg).startTime(LocalTime.of(6, 0)).endTime(LocalTime.of(6, 29)).amount(8).build();
        var tollFreeTax2 = TollFee.builder().city(gothenburg).startTime(LocalTime.of(6, 30)).endTime(LocalTime.of(6, 59)).amount(13).build();
        var tollFreeTax3 = TollFee.builder().city(gothenburg).startTime(LocalTime.of(7, 0)).endTime(LocalTime.of(7, 59)).amount(18).build();
        var tollFreeTax4 = TollFee.builder().city(gothenburg).startTime(LocalTime.of(8, 0)).endTime(LocalTime.of(8, 29)).amount(13).build();
        var tollFreeTax5 = TollFee.builder().city(gothenburg).startTime(LocalTime.of(8, 30)).endTime(LocalTime.of(14, 59)).amount(8).build();
        var tollFreeTax6 = TollFee.builder().city(gothenburg).startTime(LocalTime.of(15, 0)).endTime(LocalTime.of(15, 29)).amount(13).build();
        var tollFreeTax7 = TollFee.builder().city(gothenburg).startTime(LocalTime.of(15, 30)).endTime(LocalTime.of(16, 59)).amount(18).build();
        var tollFreeTax8 = TollFee.builder().city(gothenburg).startTime(LocalTime.of(17, 0)).endTime(LocalTime.of(17, 59)).amount(13).build();
        var tollFreeTax9 = TollFee.builder().city(gothenburg).startTime(LocalTime.of(18, 0)).endTime(LocalTime.of(18, 29)).amount(8).build();
        var tollFreeTax10 = TollFee.builder().city(gothenburg).startTime(LocalTime.of(18, 30)).endTime(LocalTime.of(5, 59)).amount(0).build();

        tollFeeRepository.saveAll(
                List.of(
                        tollFreeTax1,
                        tollFreeTax2,
                        tollFreeTax3,
                        tollFreeTax4,
                        tollFreeTax5,
                        tollFreeTax6,
                        tollFreeTax7,
                        tollFreeTax8,
                        tollFreeTax9,
                        tollFreeTax10
                )
        );
    }
}
