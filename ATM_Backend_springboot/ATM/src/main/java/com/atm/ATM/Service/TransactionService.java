package com.atm.ATM.Service;

import com.atm.ATM.Entity.Transaction;
import com.atm.ATM.Repository.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepo transactionRepo;

    public Transaction logTransaction(String cardNumber, String type, double amount, String description) {
        Transaction tx = new Transaction();
        tx.setCardNumber(cardNumber);
        tx.setType(type);
        tx.setAmount(amount);
        tx.setDescription(description);
        tx.setTimestamp(LocalDateTime.now());
        return transactionRepo.save(tx);
    }

    public List<Transaction> getHistory(String cardNumber) {
        return transactionRepo.findByCardNumber(cardNumber);
    }



}
