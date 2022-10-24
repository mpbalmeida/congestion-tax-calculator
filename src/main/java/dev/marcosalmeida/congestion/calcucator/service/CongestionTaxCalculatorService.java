package dev.marcosalmeida.congestion.calcucator.service;

import dev.marcosalmeida.congestion.calcucator.web.dto.CongestionRequestDto;
import dev.marcosalmeida.congestion.calcucator.web.dto.CongestionResponseDto;

public interface CongestionTaxCalculatorService {
    CongestionResponseDto calculateTax(CongestionRequestDto congestionRequestDto);
}
