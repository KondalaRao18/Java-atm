package com.atm.ATM.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "Account_details")
@Getter
@Setter
@NoArgsConstructor

public class UserAccount {
    @Id
    @Column(name = "card_number", unique = true, nullable = false)
    @Pattern(regexp = "\\d{12}", message = "Card number must be exactly 12 digits")
    private String cardNumber;

    @Pattern(regexp = "\\d{4}", message = "PIN must be exactly 4 digits")
    private String pin;

    @Transient
    private double balance;

    @Column(name = "encrypted_balance")
    private String encryptedBalance;

    @OneToOne(mappedBy = "userAccount", cascade = CascadeType.ALL)
    @JsonManagedReference
    private UserDetails userDetails;
}
