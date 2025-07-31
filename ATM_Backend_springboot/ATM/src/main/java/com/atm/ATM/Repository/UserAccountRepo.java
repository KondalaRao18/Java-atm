package com.atm.ATM.Repository;

import com.atm.ATM.Entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepo extends JpaRepository<UserAccount,String> {
    Optional<UserAccount> findByCardNumberAndPin(String cardNumber, String pin);
    Optional<UserAccount> findByCardNumber(String cardNumber);
}
