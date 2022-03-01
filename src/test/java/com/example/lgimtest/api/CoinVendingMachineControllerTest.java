package com.example.lgimtest.api;

import com.example.lgimtest.dto.CoinStateDto;
import com.example.lgimtest.model.CoinState;
import com.example.lgimtest.model.Denomination;
import com.example.lgimtest.service.CoinVendingMachineService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CoinVendingMachineController.class)
class CoinVendingMachineControllerTest {

    @MockBean
    CoinVendingMachineService coinVendingMachineService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    void whenCurrentStateRequestedThenCurrentStateReturned() throws Exception {

        CoinState testCoinState = new CoinState();
        testCoinState.addDenomination(new Denomination("Penny", 1));
        testCoinState.addDenomination(new Denomination("Tenpence", 10));

        testCoinState.addCoins("Penny", 15);
        testCoinState.addCoins("Tenpence", 33);

        when(coinVendingMachineService.get())
                .thenReturn(new CoinStateDto(testCoinState));

        mockMvc.perform(get("/currentState")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.denominations").isArray())
                .andExpect(jsonPath("$.denominations", hasSize(2)))

                .andExpect(jsonPath("$.denominations[1].name").value("Penny"))
                .andExpect(jsonPath("$.denominations[1].value").value("1"))

                .andExpect(jsonPath("$.denominations[0].name").value("Tenpence"))
                .andExpect(jsonPath("$.denominations[0].value").value("10"))

                .andExpect(jsonPath("$.coinAmounts.Penny").value("15"))
                .andExpect(jsonPath("$.coinAmounts.Tenpence").value("33"))

                ;

        verify(this.coinVendingMachineService, times(1))
                .get();
        verifyNoMoreInteractions(this.coinVendingMachineService);
    }

}
