package dev.marcosalmeida.congestion.calcucator.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaxResponseDto {
    private BigDecimal amount;
    private LocalDate date;
}
