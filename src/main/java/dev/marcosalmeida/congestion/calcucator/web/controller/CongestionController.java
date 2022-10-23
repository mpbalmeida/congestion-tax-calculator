package dev.marcosalmeida.congestion.calcucator.web.controller;

import dev.marcosalmeida.congestion.calcucator.service.CongestionTaxCalculatorService;
import dev.marcosalmeida.congestion.calcucator.web.dto.CongestionRequestDto;
import dev.marcosalmeida.congestion.calcucator.web.dto.CongestionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(CongestionController.BASE_URL)
@RequiredArgsConstructor
@RestController
public class CongestionController {
    public static final String BASE_URL = "/api/v1/congestion";

    private final CongestionTaxCalculatorService congestionTaxCalculatorService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CongestionResponseDto calculateCongestionTax(@RequestBody CongestionRequestDto congestionRequestDto) {
        return congestionTaxCalculatorService.calculateTax(congestionRequestDto);
    }
}
