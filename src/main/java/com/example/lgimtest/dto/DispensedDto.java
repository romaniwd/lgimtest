package com.example.lgimtest.dto;

import lombok.Data;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * DTO to be used as the representation of a coin dispense.
 */
@Getter
public class DispensedDto {

    public DispensedDto(Map<String, Integer> dispensedCoinAmounts) {
        this.dispensedCoinAmounts = dispensedCoinAmounts;
    }

    /**
     * The amounts of coins for each denomination to be dispensed.
     * The string key is a reference to the denomination name.
     */
    private Map<String, Integer> dispensedCoinAmounts;

}
