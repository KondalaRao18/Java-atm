package com.atm.ATM.Controller;

import com.atm.ATM.Entity.Transaction;
import com.atm.ATM.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/atm")
public class TransactionController {

    @Autowired
    private TransactionService service;

    @GetMapping("/transactions/{cardNumber}")
    public List<Transaction> getAll(@PathVariable String cardNumber) {
        return service.getHistory(cardNumber);
    }

    @PostMapping("/transactions/log")
    public Transaction logTransaction(@RequestBody Transaction tx) {
        return service.logTransaction(
                tx.getCardNumber(),
                tx.getType(),
                tx.getAmount(),
                tx.getDescription()
        );
    }

}