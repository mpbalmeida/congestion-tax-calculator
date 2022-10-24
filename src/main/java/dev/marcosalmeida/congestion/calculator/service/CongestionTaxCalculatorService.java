package dev.marcosalmeida.congestion.calculator.service;

import dev.marcosalmeida.congestion.calculator.web.dto.CongestionRequestDto;
import dev.marcosalmeida.congestion.calculator.web.dto.CongestionResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface CongestionTaxCalculatorService {
    int getTaxPerDay(String vehicleType, List<LocalDateTime> dates, String city);

    CongestionResponseDto calculateTax(CongestionRequestDto congestionRequestDto);
}
