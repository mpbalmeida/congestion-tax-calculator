package dev.marcosalmeida.congestion.calcucator.service.impl;

import dev.marcosalmeida.congestion.calcucator.service.CongestionTaxCalculatorService;
import dev.marcosalmeida.congestion.calcucator.web.dto.CongestionRequestDto;
import dev.marcosalmeida.congestion.calcucator.web.dto.CongestionResponseDto;
import dev.marcosalmeida.congestion.calcucator.web.dto.TaxResponseDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CongestionTaxCalculatorImpl implements CongestionTaxCalculatorService {

    private static final Map<String, Integer> tollFreeVehicles = new HashMap<>();

    static {
        tollFreeVehicles.put("motorcycle", 0);
        tollFreeVehicles.put("tractor", 1);
        tollFreeVehicles.put("emergency", 2);
        tollFreeVehicles.put("diplomat", 3);
        tollFreeVehicles.put("foreign", 4);
        tollFreeVehicles.put("military", 5);

    }

    public int getTaxPerDay(String vehicleType, List<LocalDateTime> dates) {
        LocalDateTime intervalStart = dates.get(0);
        int totalFee = 0;

        for (LocalDateTime date : dates) {
            int nextFee = GetTollFee(date, vehicleType);
            int tempFee = GetTollFee(intervalStart, vehicleType);

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

    private boolean IsTollFreeVehicle(String vehicleType) {
        if (vehicleType == null) {
            return false;
        }
        return tollFreeVehicles.containsKey(vehicleType.toLowerCase());
    }

    public int GetTollFee(LocalDateTime date, String vehicleType) {
        if (IsTollFreeDate(date) || IsTollFreeVehicle(vehicleType)) return 0;

        int hour = date.get(ChronoField.HOUR_OF_DAY);
        int minute = date.get(ChronoField.MINUTE_OF_HOUR);

        if (hour == 6 && minute >= 0 && minute <= 29) return 8;
        else if (hour == 6 && minute >= 30 && minute <= 59) return 13;
        else if (hour == 7 && minute >= 0 && minute <= 59) return 18;
        else if (hour == 8 && minute >= 0 && minute <= 29) return 13;
        else if ((hour == 8 && minute >= 30) || (hour >= 9 && hour <= 14)) return 8;
        else if (hour == 15 && minute >= 0 && minute <= 29) return 13;
        else if (hour == 15 && minute >= 0 || hour == 16 && minute <= 59) return 18;
        else if (hour == 17 && minute >= 0 && minute <= 59) return 13;
        else if (hour == 18 && minute >= 0 && minute <= 29) return 8;
        else return 0;
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
            var tax = this.getTaxPerDay(congestionRequestDto.getVehicleType(), entry.getValue().stream().sorted().toList());
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
