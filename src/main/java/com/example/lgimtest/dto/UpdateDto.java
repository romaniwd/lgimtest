package com.example.lgimtest.dto;

import lombok.Data;

import java.util.Map;

/**
 * DTO to be used for update of the current Coin State.
 */
@Data
public class UpdateDto {

    /**
     * The amounts of coins for each denomination to be added.
     * The string key is a reference to the denomination name.
     */
    private Map<String, Integer> addCoinAmounts;

}
