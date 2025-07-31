package com.atm.ATM.Controller;

import com.atm.ATM.Entity.UserAccount;
import com.atm.ATM.Service.TransactionService;
import com.atm.ATM.Service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:3000","http://127.0.0.1:5500"})
@RestController
@RequestMapping("/atm")
public class UserAccountController {
    @Autowired
    private UserAccountService service;

//    @PostMapping("/register")
//    @CrossOrigin(origins = "*")
//    public UserAccount create(@RequestBody UserAccount userAccount) {
//        return service.register(userAccount);
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {
        UserAccount acc = service.authenticate(req.get("cardNumber"), req.get("pin"));
        return acc != null ? ResponseEntity.ok(acc) : ResponseEntity.status(401).body("Invalid credentials");
    }

    @GetMapping("/balance/{cardNumber}")
    public double getBalance(@PathVariable String cardNumber) {
        return service.getBalance(cardNumber);
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody Map<String, String> req) {
        String result = service.deposit(req.get("cardNumber"), Double.parseDouble(req.get("amount")));
        return ResponseEntity.ok(result);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody Map<String, String> req) {
        String result = service.withdraw(req.get("cardNumber"), Double.parseDouble(req.get("amount")));
        return ResponseEntity.ok(result);
    }

    @PutMapping("/change-pin/{cardNumber}")
    public ResponseEntity<?> changePin(@PathVariable String cardNumber, @RequestBody Map<String, String> req) {
        String result = service.changePIN(cardNumber, req.get("newPin"));
        if ("PIN changed successfully".equals(result)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
}
