package com.example.lgimtest.service;

import com.example.lgimtest.dto.CoinStateDto;
import com.example.lgimtest.dto.DispensedDto;
import com.example.lgimtest.dto.InitialisationDto;
import com.example.lgimtest.dto.UpdateDto;
import com.example.lgimtest.model.CoinState;
import com.example.lgimtest.model.Denomination;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Coin Vending Mcahine service.
 */
@Service
public class CoinVendingMachineService {

    private CoinState currentCoinState;

    /**
     * Get the coin state.
     * @return the current coin state.
     */
    public CoinStateDto get() {
        return new CoinStateDto(currentCoinState);
    }

    /**
     * Initialise the coin state.
     * @param initialisationDto Values to be used to initialise the coin state.
     * @return the initialised coin state.
     */
    public CoinStateDto initialise(InitialisationDto initialisationDto) {

        if (currentCoinState != null) {
            throw new IllegalStateException("Vending Machine is already initialised. Cannot initialise it again.");
        }

        validateInitialisationDto(initialisationDto);

        currentCoinState = new CoinState();
        initialisationDto.getDenominations()
                .forEach(d-> currentCoinState.addDenomination(d));

        if (initialisationDto.getInitialCoinAmounts() != null) {
            for (String key : initialisationDto.getInitialCoinAmounts().keySet()) {
                currentCoinState.addCoins(key, initialisationDto.getInitialCoinAmounts().get(key));
            }
        }

        return new CoinStateDto(currentCoinState);
    }

    /**
     * Update the coin state.
     * @param updateDto Dto with coins data to be updated to the current machine state.
     * @return the updated coin state.
     */
    public CoinStateDto update(UpdateDto updateDto) {

        if (currentCoinState == null) {
            throw new IllegalStateException("Vending Machine has not been initialised. Cannot update it.");
        }

        for (String key : updateDto.getAddCoinAmounts().keySet()) {
            currentCoinState.addCoins(key, updateDto.getAddCoinAmounts().get(key));
        }

        return new CoinStateDto(currentCoinState);
    }


    /**
     * Dispense coins for the amount requested.
     * @param amountRequested Amount requested to be dispensed.
     * @return the coins dispensed.
     */
    public DispensedDto dispense(Integer amountRequested) {

        if (currentCoinState == null) {
            throw new IllegalStateException("Vending Machine has not been initialised. Cannot dispense anything.");
        }

        if (amountRequested == null || amountRequested <= 0) {
            throw new IllegalStateException("Cannot dispense a negative or zero amount.");
        }

        int[] availableCoinDenominations = currentCoinState.getAvailableCoinDenominations();
        int[] availableCoinQuantities = currentCoinState.getAvailableCoinQuantities();

        List<Integer> coinsToDispense = new ArrayList<>();

        // create a list of coins by denomination to be dispensed (removed) from our vending machine
        createCoinsToDispense(amountRequested, availableCoinDenominations, availableCoinQuantities, coinsToDispense, 0);

        if (coinsToDispense.isEmpty()) {
            throw new IllegalStateException("Cannot dispense correct amount with available coins.");
        }

        Map<String, Integer> dispensedCoinAmounts = new HashMap<>();

        for (int coinDenomination : coinsToDispense) {
            Denomination denomination = currentCoinState.getDenominationForValue(coinDenomination);
            dispensedCoinAmounts.merge(denomination.getName(), 1, Integer::sum);
            currentCoinState.removeCoinByDenominationValue(coinDenomination);
        }
        return new DispensedDto(dispensedCoinAmounts);
    }

    /**
     * Validate the initialisation Dto.
     * @param initialisationDto to be validated.
     */
    private void validateInitialisationDto(InitialisationDto initialisationDto) {

        List<String> names = initialisationDto.getDenominations()
                .stream()
                .map(Denomination::getName)
                .collect(Collectors.toList());

        // check for unique names and values. works by trying to add a list element to a set. Allegedly the most efficient way
        // see https://stackoverflow.com/questions/30053487/how-to-check-if-exists-any-duplicate-in-java-8-streams

        boolean namesUnique = names
                .stream()
                .allMatch(new HashSet<>()::add);
        if (!namesUnique) {
            throw new IllegalStateException("Denomination names are not unique. Cannot initialise coin vending machine.");
        }

        boolean valuesUnique = initialisationDto.getDenominations()
                .stream()
                .map(Denomination::getValue)
                .allMatch(new HashSet<>()::add);
        if (!valuesUnique) {
            throw new IllegalStateException("Denomination values are not unique. Cannot initialise coin vending machine.");
        }

        // check that the string refs to a denomination are found
        if (initialisationDto.getInitialCoinAmounts() != null) {
            for (String key : initialisationDto.getInitialCoinAmounts().keySet()) {
                if (!names.contains(key)) {
                    throw new IllegalStateException(String.format("Key %s for Denomination is not found. Cannot initialise coin vending machine.", key));
                }
            }
        }
    }

    /**
     * Check a lists values are unique.
     * @param list the list to check for unique values.
     * @param <T> the type of the list.
     * @return true if all are unique.
     */
    private <T> boolean  areValuesUnique(List<T> list) {
        // works by trying to add a list element to a set. Allegedly the most efficient way
        // see https://stackoverflow.com/questions/30053487/how-to-check-if-exists-any-duplicate-in-java-8-streams
        return list
                .stream()
                .allMatch(new HashSet<>()::add);
    }

    private int createCoinsToDispense(int targetValue, int[] coinDenominations, int[] coinQuantities, List<Integer> listOfCoins, int position) {

        //if total is 0 then return
        if (targetValue == 0) {
            return 0;
        }

        int pushedToList = 0;
        for (int counter = position; counter < coinDenominations.length; counter++) {

            int coinDenomination = coinDenominations[counter];

            //Continue if coin is greater than the total
            if (coinDenomination > targetValue) {
                continue;
            }

            // Continue if the coin isnt available to dispense as change
            if (coinQuantities[counter] == 0) {
                continue;
            }

            coinQuantities[counter] -= 1;
            listOfCoins.add(coinDenomination);
            pushedToList = coinDenomination;
            targetValue -= coinDenomination;

            targetValue = createCoinsToDispense(targetValue, coinDenominations, coinQuantities, listOfCoins, counter);
            if (targetValue > 0) {
                if (pushedToList > 0) {
                    targetValue += pushedToList;
                    listOfCoins.remove(listOfCoins.size() - 1);
                    pushedToList = 0;
                }
            }
        }
        return targetValue;
    }
}
