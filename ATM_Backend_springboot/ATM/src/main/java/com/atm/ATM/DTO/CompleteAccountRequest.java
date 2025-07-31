package com.atm.ATM.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompleteAccountRequest {
    private String cardNumber;
    private String pin;
    private Double balance;
    private UserDetailsRequest userDetails;

    @Override
    public String toString() {
        return "CompleteAccountRequest{" +
                "cardNumber='" + cardNumber + '\'' +
                ", pin='****'" +
                ", balance=" + balance +
                ", userDetails=" + userDetails +
                '}';
    }
}