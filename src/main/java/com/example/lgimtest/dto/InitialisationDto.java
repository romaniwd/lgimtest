package com.example.lgimtest.dto;

import com.example.lgimtest.model.Denomination;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * DTO to be used for initialisation of the current Coin State.
 */
@Data
public class InitialisationDto {

    /**
     * The denominations to be set up.
     * This is a List not a Set so that we can provide our own validation.
     */
    private List<Denomination> denominations;

    /**
     * The initial amounts of coins for each denomination.
     * The string key is a reference to the denomination name.
     */
    private Map<String, Integer> initialCoinAmounts;

}
