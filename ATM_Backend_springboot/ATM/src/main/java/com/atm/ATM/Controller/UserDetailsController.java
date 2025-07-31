package com.atm.ATM.Controller;

import com.atm.ATM.Entity.UserDetails;
import com.atm.ATM.Service.UserDetailsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/atm")
public class UserDetailsController {
    @Autowired
    private UserDetailsService service;

    @PostMapping("/add")
    public ResponseEntity<UserDetails> add(@Valid @RequestBody UserDetails user) {
        UserDetails savedUsers = service.saveDetails(user);
        return ResponseEntity.ok(savedUsers);
    }

    @GetMapping("/get/{id}")
    public UserDetails get(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/all")
    public List<UserDetails> getAll() {
        return service.getAll();
    }


    @GetMapping("/user/by-card/{cardNumber}")
    public ResponseEntity<?> getUserByCardNumber(@PathVariable String cardNumber) {
        try {
            UserDetails userDetails = service.getDetailsByCardNumber(cardNumber);
            if (userDetails == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User not found for card number: " + cardNumber);
            }
            return ResponseEntity.ok(userDetails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving user: " + e.getMessage());
        }
    }

    @PutMapping("/update/by-card/{cardNumber}")
    public ResponseEntity<?> updateUserByCardNumber(@PathVariable String cardNumber, @RequestBody UserDetails updatedDetails) {
        try {
            UserDetails updatedUser = service.updateDetailsByCardNumber(cardNumber, updatedDetails);
            if (updatedUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User not found for card number: " + cardNumber);
            }
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating user: " + e.getMessage());
        }
    }
//    public ResponseEntity<?> update(@PathVariable )
}
