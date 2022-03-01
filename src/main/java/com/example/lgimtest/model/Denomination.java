package com.example.lgimtest.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Denomination {

    /**
     * The name of the coin.
     * For example penny
     */
    private String name;

    /**
     * The value of the denomination.
     * For example, a 50 pence piece would be 50
     */
    private Integer value;

}
