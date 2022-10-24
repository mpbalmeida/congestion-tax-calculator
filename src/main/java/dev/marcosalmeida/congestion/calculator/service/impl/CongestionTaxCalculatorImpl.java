package dev.marcosalmeida.congestion.calculator.service.impl;

import dev.marcosalmeida.congestion.calculator.repository.TollFeeRepository;
import dev.marcosalmeida.congestion.calculator.repository.TollFreeVehiclesRepository;
import dev.marcosalmeida.congestion.calculator.service.CongestionTaxCalculatorService;
import dev.marcosalmeida.congestion.calculator.web.dto.CongestionRequestDto;
import dev.marcosalmeida.congestion.calculator.web.dto.CongestionResponseDto;
import dev.marcosalmeida.congestion.calculator.web.dto.TaxResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CongestionTaxCalculatorImpl implements CongestionTaxCalculatorService {

    private final TollFreeVehiclesRepository tollFreeVehiclesRepository;
    private final TollFeeRepository tollFeeRepository;

    public int getTaxPerDay(String vehicleType, List<LocalDateTime> dates, String city) {
        LocalDateTime intervalStart = dates.get(0);
        int totalFee = 0;

        for (LocalDateTime date : dates) {
            int nextFee = GetTollFee(date, vehicleType, city);
            int tempFee = GetTollFee(intervalStart, vehicleType, city);

            long diffInMillies = date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - intervalStart.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            long minutes = diffInMillies / 1000 / 60;

            if (minutes <= 60) {
                if (totalFee > 0) totalFee -= tempFee;
                if (nextFee >= tempFee) tempFee = nextFee;
                totalFee += tempFee;
            } else {
                totalFee += nextFee;
            }
        }

        if (totalFee > 60) totalFee = 60;
        return totalFee;
    }

    private boolean IsTollFreeVehicle(String vehicleType, String city) {
        if (vehicleType == null || city == null) {
            return false;
        }
        return tollFreeVehiclesRepository.findByCityAndType(vehicleType, city).isPresent();
    }

    public int GetTollFee(LocalDateTime date, String vehicleType, String city) {
        if (IsTollFreeDate(date) || IsTollFreeVehicle(vehicleType, city)) return 0;

        int hour = date.get(ChronoField.HOUR_OF_DAY);
        int minute = date.get(ChronoField.MINUTE_OF_HOUR);

        return tollFeeRepository.findFeeByCityAndTime(LocalTime.of(hour, minute), city).orElse(0);
    }

    private Boolean IsTollFreeDate(LocalDateTime date) {
        int year = date.getYear();
        int month = date.get(ChronoField.MONTH_OF_YEAR);
        int day = date.get(ChronoField.DAY_OF_WEEK);
        int dayOfMonth = date.get(ChronoField.DAY_OF_MONTH);

        if (day == Calendar.SATURDAY || day == Calendar.SUNDAY) return true;

        if (year == 2013) {
            return (month == 1 && dayOfMonth == 1) ||
                    (month == 3 && (dayOfMonth == 28 || dayOfMonth == 29)) ||
                    (month == 4 && (dayOfMonth == 1 || dayOfMonth == 30)) ||
                    (month == 5 && (dayOfMonth == 1 || dayOfMonth == 8 || dayOfMonth == 9)) ||
                    (month == 6 && (dayOfMonth == 5 || dayOfMonth == 6 || dayOfMonth == 21)) ||
                    (month == 7) ||
                    (month == 11 && dayOfMonth == 1) ||
                    (month == 12 && (dayOfMonth == 24 || dayOfMonth == 25 || dayOfMonth == 26 || dayOfMonth == 31));
        }
        return false;
    }

    @Override
    public CongestionResponseDto calculateTax(CongestionRequestDto congestionRequestDto) {

        // Map the input to a Map of date and list of passes from the same day
        var mapByDate = congestionRequestDto.getPasses().stream().collect(Collectors.groupingBy(localDateTime ->
                localDateTime.truncatedTo(ChronoUnit.DAYS).withDayOfYear(localDateTime.getDayOfYear()).toLocalDate()
        ));

        int total = 0;
        Set<TaxResponseDto> details = new HashSet<>();

        for (Map.Entry<LocalDate, List<LocalDateTime>> entry : mapByDate.entrySet()) {
            // calculate the tax per day with sorted passes
            var tax = this.getTaxPerDay(congestionRequestDto.getVehicleType(), entry.getValue().stream().sorted().toList(), congestionRequestDto.getCity());
            total += tax;

            // add details to response list
            details.add(TaxResponseDto.builder().amount(tax).date(entry.getKey()).build());
        }

        return CongestionResponseDto.builder()
                .total(total)
                .details(details)
                .build();
    }
}
