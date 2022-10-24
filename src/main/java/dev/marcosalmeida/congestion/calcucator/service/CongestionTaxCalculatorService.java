package dev.marcosalmeida.congestion.calcucator.service;

import dev.marcosalmeida.congestion.calcucator.web.dto.CongestionRequestDto;
import dev.marcosalmeida.congestion.calcucator.web.dto.CongestionResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface CongestionTaxCalculatorService {
    int getTaxPerDay(String vehicleType, List<LocalDateTime> dates);

    CongestionResponseDto calculateTax(CongestionRequestDto congestionRequestDto);
}
