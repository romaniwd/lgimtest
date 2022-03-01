package com.example.lgimtest.dto;

import com.example.lgimtest.model.CoinState;
import com.example.lgimtest.model.Denomination;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DTO to be used for the current Coin State.
 */
@Getter
public class CoinStateDto {

    public CoinStateDto(CoinState coinState) {
        this.coinAmounts = new HashMap<>();
        this.denominations = new ArrayList<>();

        if (coinState != null) {
            this.denominations.addAll(coinState.getDenominations());
            for (Denomination denomination : coinState.getCoins().keySet()) {
                coinAmounts.put(denomination.getName(), coinState.getCoins().get(denomination));
            }
        }
    }


    /**
     * The denominations.
     */
    private List<Denomination> denominations;

    /**
     * The amounts of coins for each denomination.
     * The string key is a reference to the denomination name.
     */
    private Map<String, Integer> coinAmounts;

}
