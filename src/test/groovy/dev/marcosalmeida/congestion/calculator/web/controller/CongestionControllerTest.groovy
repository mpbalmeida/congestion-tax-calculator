package dev.marcosalmeida.congestion.calculator.web.controller

import com.fasterxml.jackson.databind.ObjectMapper
import dev.marcosalmeida.congestion.calculator.service.CongestionTaxCalculatorService
import dev.marcosalmeida.congestion.calculator.web.dto.CongestionRequestDto
import dev.marcosalmeida.congestion.calculator.web.dto.CongestionResponseDto
import dev.marcosalmeida.congestion.calculator.web.dto.TaxResponseDto
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime

import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.hasSize
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(CongestionController)
class CongestionControllerTest extends Specification {

    @SpringBean
    CongestionTaxCalculatorService congestionTaxCalculatorService = Mock(CongestionTaxCalculatorService)

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    def "CalculateCongestionTax"() {
        given:
        def request = CongestionRequestDto.builder().vehicleType("car").passes(Set.of(LocalDateTime.now())).build()
        def requestString = objectMapper.writeValueAsString(request)
        def expectedResult = CongestionResponseDto.builder()
                .total(Integer.valueOf(100))
                .details(Set.of(TaxResponseDto.builder().amount(Integer.valueOf(100)).date(LocalDate.now()).build()))
                .build()

        when:
        mockMvc.perform(post(CongestionController.BASE_URL)
                .content(requestString)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath('$.total', equalTo(expectedResult.total.intValue())),
                        jsonPath('$.details', hasSize(expectedResult.getDetails().size())),
                        jsonPath('$.details[0].amount', equalTo(expectedResult.getDetails()[0].getAmount().intValue())),
                        jsonPath('$.details[0].date', equalTo(expectedResult.getDetails()[0].getDate().toString()))
                )

        then:
        1 * congestionTaxCalculatorService.calculateTax(_) >> expectedResult
        0 * _
    }
}
