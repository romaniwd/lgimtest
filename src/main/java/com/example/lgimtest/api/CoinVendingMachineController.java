package com.example.lgimtest.api;

import com.example.lgimtest.dto.CoinStateDto;
import com.example.lgimtest.dto.DispensedDto;
import com.example.lgimtest.dto.InitialisationDto;
import com.example.lgimtest.dto.UpdateDto;
import com.example.lgimtest.model.CoinState;
import com.example.lgimtest.service.CoinVendingMachineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest Controller to provide api layer for the coin vending machine.
 */
@RestController
@RequiredArgsConstructor
public class CoinVendingMachineController {

    private final CoinVendingMachineService coinVendingMachineService;

    /**
     * Get the current Coin State.
     * @return The current coin state of the vending machine
     */
    @GetMapping("/currentState")
    public ResponseEntity<CoinStateDto> get() {
        return ResponseEntity.ok(coinVendingMachineService.get());
    }

    /**
     * Initialise the Coin State.
     * @param initialisationDto The current coin state will be overridden by this new value.
     * @return The current coin state of the vending machine
     */
    @PostMapping("/initialise")
    public ResponseEntity<CoinStateDto> initialise(@RequestBody InitialisationDto initialisationDto) {
        return ResponseEntity.ok(coinVendingMachineService.initialise(initialisationDto));
    }

    /**
     * Update the Coin State.
     * @param updateDto The current coin state will be updated with the values from this parameter.
     * @return The current coin state of the vending machine
     */
    @PutMapping("/update")
    public ResponseEntity<CoinStateDto> update(@RequestBody UpdateDto updateDto) {
        return ResponseEntity.ok(coinVendingMachineService.update(updateDto));
    }

    /**
     * Dispense coins.
     * @param amountRequested The requested amount to be dispensed.
     * @return The coins to be dispensed.
     */
    @PutMapping("/dispense")
    public ResponseEntity<DispensedDto> dispense(@RequestBody Integer amountRequested) {
        return ResponseEntity.ok(coinVendingMachineService.dispense(amountRequested));
    }
}
