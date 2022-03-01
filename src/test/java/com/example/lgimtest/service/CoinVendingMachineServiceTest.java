package com.example.lgimtest.service;

import com.example.lgimtest.dto.CoinStateDto;
import com.example.lgimtest.dto.DispensedDto;
import com.example.lgimtest.dto.InitialisationDto;
import com.example.lgimtest.dto.UpdateDto;
import com.example.lgimtest.model.Denomination;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CoinVendingMachineServiceTest {

    private CoinVendingMachineService coinVendingMachineService;

    @BeforeEach
    void setup() {
        coinVendingMachineService = new CoinVendingMachineService();
    }

    @Test
    void testServiceInitialisesOk() {

        CoinStateDto dto = coinVendingMachineService.initialise(setupInitialisationDto());

        assertNotNull(dto);
        assertEquals(3, dto.getDenominations().size());
        assertEquals(3, dto.getCoinAmounts().size());

    }

    @Test
    void testServiceUpdateWithoutInitialisationCausesException() {

        IllegalStateException result = assertThrows(IllegalStateException.class, () -> {
            coinVendingMachineService.update(new UpdateDto());
        });
        assertEquals("Vending Machine has not been initialised. Cannot update it.", result.getMessage());
    }

    @Test
    void testServiceUpdateOk() {

        coinVendingMachineService.initialise(setupInitialisationDto());
        CoinStateDto dto = coinVendingMachineService.update(setupUpdateDto());
        assertNotNull(dto);
        assertNotNull(dto.getCoinAmounts());
        assertNotNull(dto.getCoinAmounts().get("penny"));
        assertNotNull(dto.getCoinAmounts().get("tuppence"));
        assertNotNull(dto.getCoinAmounts().get("pound"));

        assertEquals(30+2, dto.getCoinAmounts().get("penny"));
        assertEquals(25+1, dto.getCoinAmounts().get("tuppence"));
        assertEquals(15+5, dto.getCoinAmounts().get("pound"));

    }

    @Test
    void testServiceDispenseOk() {

        coinVendingMachineService.initialise(setupInitialisationDto());
        DispensedDto dto = coinVendingMachineService.dispense(131);
        assertNotNull(dto);
        assertEquals(1, dto.getDispensedCoinAmounts().get("penny"));
        assertEquals(15, dto.getDispensedCoinAmounts().get("tuppence"));
        assertEquals(1, dto.getDispensedCoinAmounts().get("pound"));

        CoinStateDto revisedCoinStateDto = coinVendingMachineService.get();
        assertNotNull(revisedCoinStateDto.getCoinAmounts());
        assertNotNull(revisedCoinStateDto.getCoinAmounts().get("penny"));
        assertNotNull(revisedCoinStateDto.getCoinAmounts().get("tuppence"));
        assertNotNull(revisedCoinStateDto.getCoinAmounts().get("pound"));

        assertEquals(30-1, revisedCoinStateDto.getCoinAmounts().get("penny"));
        assertEquals(25-15, revisedCoinStateDto.getCoinAmounts().get("tuppence"));
        assertEquals(15-1, revisedCoinStateDto.getCoinAmounts().get("pound"));

    }

    @Test
    void testServiceNotDispensableCausesException() {

        coinVendingMachineService.initialise(setupInitialisationDto());

        IllegalStateException result = assertThrows(IllegalStateException.class, () -> {
            coinVendingMachineService.dispense(3004);
        });
        assertEquals("Cannot dispense correct amount with available coins.", result.getMessage());
    }

    private InitialisationDto setupInitialisationDto() {
        InitialisationDto initDto = new InitialisationDto();
        List<Denomination> denominations = new ArrayList<>();
        denominations.add(new Denomination("penny", 1));
        denominations.add(new Denomination("tuppence", 2));
        denominations.add(new Denomination("pound", 100));

        initDto.setDenominations(denominations);

        Map<String,Integer> coins = new HashMap<>();
        coins.put("penny", 30);
        coins.put("tuppence", 25);
        coins.put("pound", 15);
        initDto.setInitialCoinAmounts(coins);
        return initDto;
    }

    private UpdateDto setupUpdateDto() {
        UpdateDto updateDto = new UpdateDto();

        Map<String,Integer> coins = new HashMap<>();
        coins.put("penny", 2);
        coins.put("tuppence", 1);
        coins.put("pound", 5);
        updateDto.setAddCoinAmounts(coins);
        return updateDto;
    }
}
