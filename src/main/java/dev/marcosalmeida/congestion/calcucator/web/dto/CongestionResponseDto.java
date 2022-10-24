package dev.marcosalmeida.congestion.calcucator.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CongestionResponseDto {
    private BigDecimal total;
    private Set<TaxResponseDto> details;
}
