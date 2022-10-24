package dev.marcosalmeida.congestion.calcucator.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaxResponseDto {
    private Integer amount;

    private LocalDate date;
}
