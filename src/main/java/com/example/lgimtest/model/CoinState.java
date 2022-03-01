package com.example.lgimtest.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class CoinState {

    /**
     * Set of available denominations;
     */
    private Set<Denomination> denominations = new HashSet<>();

    /**
     * Map of the coins currently in the machine.
     */
    private Map<Denomination, Integer> coins = new HashMap<>();

    /**
     * Add a denomination to the state.
     * @param denomination the denomination to add.
     */
    public void addDenomination(Denomination denomination) {
        denominations.add(denomination);
    }

    /**
     * Add coins to the current state.
     * @param denominationKey Key of the denomination to add coins for.
     * @param amount the amount of coins to be added for the denomination.
     */
    public void addCoins(String denominationKey, Integer amount) {
        Denomination denomination = getDenominationForKey(denominationKey);
        Integer currentValue = coins.get(denomination);
        if (currentValue == null) {
            coins.put(denomination, amount);
        }
        else {
            coins.put(denomination, currentValue + amount);
        }
    }

    /**
     * Get Denomination by its name key.
     * @param key name of the denomination
     * @return the found Denomination or null if not found
     */
    public Denomination getDenominationForKey(String key) {
        return denominations
                .stream()
                .filter(d -> d.getName().equals(key))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get Denomination by its denomination value.
     * @param value denomination value to find
     * @return the found Denomination or null if not found
     */
    public Denomination getDenominationForValue(Integer value) {
        return denominations
                .stream()
                .filter(d -> d.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }

    /**
     * Remove a coin for the given value denomination.
     * @param denominationValue denomination value to remove a coin for.
     */
    public void removeCoinByDenominationValue(Integer denominationValue) {
        Denomination denomination = getDenominationForValue(denominationValue);
        Integer previous = coins.put(denomination, coins.get(denomination) - 1);
        if (previous == 1) {
            coins.remove(denomination);
        }
    }

    /**
     * Get available denominations as an integer array of the denomination values.
     * @return denomination values.
     */
    public int[] getAvailableCoinDenominations() {

        List<Integer> availableDenominations = new ArrayList<>();
        for (Denomination denomination : getSortedDenominationsFromCoins()) {
            availableDenominations.add(denomination.getValue());
        }
        return availableDenominations.stream().mapToInt(Integer::intValue).toArray();
    }

    /**
     * Get available coin quantities of all coins.
     * @return coin quantities
     */
    public int[] getAvailableCoinQuantities() {

        List<Integer> availableQuantities = new ArrayList<>();
        for (Denomination denomination : getSortedDenominationsFromCoins()) {
            availableQuantities.add(coins.get(denomination));
        }
        return availableQuantities.stream().mapToInt(Integer::intValue).toArray();
    }

    private List<Denomination> getSortedDenominationsFromCoins() {
        return coins.keySet()
                .stream()
                .sorted(Comparator.comparing(Denomination::getValue).reversed())
                .collect(Collectors.toList());
    }
}
