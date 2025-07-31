package com.atm.ATM.Repository;

import com.atm.ATM.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCardNumber(String cardNumber);
    List<Transaction> findByCardNumberOrderByTimestampDesc(String cardNumber);
}
