package dev.marcosalmeida.congestion.calcucator.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CongestionResponseDto {
    private Integer total;
    private Set<TaxResponseDto> details;
}
