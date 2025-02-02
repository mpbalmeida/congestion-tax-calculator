package dev.marcosalmeida.congestion.calculator.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CongestionRequestDto {
    private String vehicleType;
    private String city;

    private Set<LocalDateTime> passes;
}
