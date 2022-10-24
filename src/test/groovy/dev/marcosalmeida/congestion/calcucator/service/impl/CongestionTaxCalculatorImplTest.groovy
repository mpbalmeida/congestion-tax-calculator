package dev.marcosalmeida.congestion.calcucator.service.impl

import dev.marcosalmeida.congestion.calcucator.web.dto.CongestionRequestDto
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime

class CongestionTaxCalculatorImplTest extends Specification {

    CongestionTaxCalculatorImpl service

    void setup() {
        service = new CongestionTaxCalculatorImpl()
    }

    @Unroll
    def "getTax for #vehicleType and #localDates"(String vehicleType, List<LocalDateTime> localDates) {
        given:
        def tax = service.getTaxPerDay(vehicleType, localDates)

        expect:
        tax == expectedTax

        where:
        localDates                                         | vehicleType || expectedTax
        List.of(LocalDateTime.of(2013, 1, 14, 21, 0, 0))   | "car"       || 0 // 18:30–05:59
        List.of(LocalDateTime.of(2013, 1, 15, 21, 0, 0))   | "car"       || 0 // 18:30–05:59
        List.of(
                LocalDateTime.of(2013, 2, 7, 6, 23, 27),
                LocalDateTime.of(2013, 2, 7, 15, 27, 0))   | "car"       || 21 // 06:00–06:29 + 15:00–15:29	(13+8)
        List.of(
                LocalDateTime.of(2013, 2, 8, 6, 27, 0),
                LocalDateTime.of(2013, 2, 8, 6, 20, 27),
                LocalDateTime.of(2013, 2, 8, 14, 35, 0),
                LocalDateTime.of(2013, 2, 8, 15, 29, 0),
                LocalDateTime.of(2013, 2, 8, 15, 47, 0),
                LocalDateTime.of(2013, 2, 8, 16, 01, 0),
                LocalDateTime.of(2013, 2, 8, 16, 48, 0),
                LocalDateTime.of(2013, 2, 8, 17, 49, 0),
                LocalDateTime.of(2013, 2, 8, 18, 29, 0),
                LocalDateTime.of(2013, 2, 8, 18, 35, 0))   | "car"       || 60
        List.of(LocalDateTime.of(2013, 3, 26, 14, 25, 0))  | "car"       || 8 // 08:30–14:59
        List.of(LocalDateTime.of(2013, 3, 28, 14, 07, 27)) | "car"       || 0 // National holiday
        List.of(
                LocalDateTime.of(2013, 2, 8, 6, 27, 0),
                LocalDateTime.of(2013, 2, 8, 6, 20, 27),
                LocalDateTime.of(2013, 2, 8, 14, 35, 0),
                LocalDateTime.of(2013, 2, 8, 15, 29, 0),
                LocalDateTime.of(2013, 2, 8, 15, 47, 0),
                LocalDateTime.of(2013, 2, 8, 16, 01, 0),
                LocalDateTime.of(2013, 2, 8, 16, 48, 0),
                LocalDateTime.of(2013, 2, 8, 17, 49, 0),
                LocalDateTime.of(2013, 2, 8, 18, 29, 0),
                LocalDateTime.of(2013, 2, 8, 18, 35, 0))   | "Diplomat"  || 0 // Diplomat car
    }

    @Unroll
    def "calculateTax"(String vehicleType, Set<LocalDateTime> localDates) {

        given:
        def response = service.calculateTax(CongestionRequestDto.builder().vehicleType(vehicleType).passes(localDates).build())

        expect:
        response.getTotal() == expectedTotal
        response.getDetails().size() == expectedDetailsSize


        where:
        localDates                                         | vehicleType || expectedTotal | expectedDetailsSize
        List.of(
                LocalDateTime.of(2013, 1, 14, 21, 0, 0),
                LocalDateTime.of(2013, 1, 15, 21, 0, 0),

                LocalDateTime.of(2013, 2, 7, 6, 23, 27),
                LocalDateTime.of(2013, 2, 7, 15, 27, 0),

                LocalDateTime.of(2013, 2, 8, 6, 27, 0),
                LocalDateTime.of(2013, 2, 8, 6, 20, 27),
                LocalDateTime.of(2013, 2, 8, 14, 35, 0),
                LocalDateTime.of(2013, 2, 8, 15, 29, 0),
                LocalDateTime.of(2013, 2, 8, 15, 47, 0),
                LocalDateTime.of(2013, 2, 8, 16, 01, 0),
                LocalDateTime.of(2013, 2, 8, 16, 48, 0),
                LocalDateTime.of(2013, 2, 8, 17, 49, 0),
                LocalDateTime.of(2013, 2, 8, 18, 29, 0),
                LocalDateTime.of(2013, 2, 8, 18, 35, 0),
                LocalDateTime.of(2013, 3, 26, 14, 25, 0),
                LocalDateTime.of(2013, 3, 28, 14, 07, 27)) | "car"       || 89            | 6
    }
}
