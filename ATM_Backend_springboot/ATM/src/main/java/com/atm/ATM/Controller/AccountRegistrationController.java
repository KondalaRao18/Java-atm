package com.atm.ATM.Controller;

import com.atm.ATM.DTO.CompleteAccountRequest;
import com.atm.ATM.Entity.UserAccount;
import com.atm.ATM.Entity.UserDetails;
import com.atm.ATM.Repository.UserAccountRepo;
import com.atm.ATM.Service.UserDetailsService;
import com.atm.ATM.Util.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/atm")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AccountRegistrationController {

    @Autowired
    private UserAccountRepo userAccountRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private EncryptionUtil encryptionUtil;

    @PostMapping("/register")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> createCompleteAccount(@RequestBody CompleteAccountRequest request) {
        try {
            System.out.println("=== BACKEND: Received registration request ===");
            System.out.println("Card Number: " + request.getCardNumber());
            System.out.println("PIN: ****");
            System.out.println("Balance: " + request.getBalance());
            System.out.println("User Details: " + (request.getUserDetails() != null ? "Present" : "Missing"));

            // Validate card number
            if (request.getCardNumber() == null || !request.getCardNumber().matches("\\d{12}")) {
                System.out.println("ERROR: Invalid card number format");
                return ResponseEntity.badRequest().body(Map.of("error", "Card number must be exactly 12 digits"));
            }

            // Check if card number already exists
            if (userAccountRepository.existsById(request.getCardNumber())) {
                System.out.println("ERROR: Card number already exists");
                return ResponseEntity.badRequest().body(Map.of("error", "Card number already exists"));
            }

            // Validate PIN
            if (request.getPin() == null || !request.getPin().matches("\\d{4}")) {
                System.out.println("ERROR: Invalid PIN format");
                return ResponseEntity.badRequest().body(Map.of("error", "PIN must be exactly 4 digits"));
            }

            // Validate balance
            if (request.getBalance() == null || request.getBalance() < 100) {
                System.out.println("ERROR: Invalid balance amount");
                return ResponseEntity.badRequest().body(Map.of("error", "Minimum balance is ₹100"));
            }

            // Validate user details
            if (request.getUserDetails() == null) {
                System.out.println("ERROR: User details missing");
                return ResponseEntity.badRequest().body(Map.of("error", "User details are required"));
            }

            if (request.getUserDetails().getFullName() == null || request.getUserDetails().getFullName().trim().isEmpty()) {
                System.out.println("ERROR: Full name missing");
                return ResponseEntity.badRequest().body(Map.of("error", "Full name is required"));
            }

            if (request.getUserDetails().getEmail() == null || request.getUserDetails().getEmail().trim().isEmpty()) {
                System.out.println("ERROR: Email missing");
                return ResponseEntity.badRequest().body(Map.of("error", "Email is required"));
            }

            System.out.println("All validations passed. Creating user details...");

            // Step 1: Create UserDetails mapping to your actual entity fields
            UserDetails userDetails = new UserDetails();

            // Map frontend fields to your entity fields
            userDetails.setName(request.getUserDetails().getFullName().trim()); // fullName -> name
            userDetails.setDateOfBirth(request.getUserDetails().getDob()); // dob -> dateOfBirth
            userDetails.setContactNumber(request.getUserDetails().getPhone()); // phone -> contactNumber
            userDetails.setEmail(request.getUserDetails().getEmail().trim().toLowerCase());
            userDetails.setAddress(request.getUserDetails().getAddress() != null ? request.getUserDetails().getAddress().trim() : "");

            // Calculate age from date of birth
            if (request.getUserDetails().getDob() != null && !request.getUserDetails().getDob().isEmpty()) {
                try {
                    LocalDate birthDate = LocalDate.parse(request.getUserDetails().getDob());
                    LocalDate currentDate = LocalDate.now();
                    int calculatedAge = Period.between(birthDate, currentDate).getYears();
                    userDetails.setAge(String.valueOf(calculatedAge));
                    System.out.println("Calculated age: " + calculatedAge);
                } catch (Exception e) {
                    System.out.println("Error calculating age, setting to 0: " + e.getMessage());
                    userDetails.setAge("0");
                }
            } else {
                userDetails.setAge("0");
            }

            System.out.println("Mapped user details:");
            System.out.println("Name: " + userDetails.getName());
            System.out.println("Date of Birth: " + userDetails.getDateOfBirth());
            System.out.println("Age: " + userDetails.getAge());
            System.out.println("Contact Number: " + userDetails.getContactNumber());
            System.out.println("Email: " + userDetails.getEmail());
            System.out.println("Address: " + userDetails.getAddress());

            // Save user details using your service
            UserDetails savedUserDetails = userDetailsService.saveDetails(userDetails);
            System.out.println("User details saved with ID: " + savedUserDetails.getId());

            // Step 2: Create UserAccount
            UserAccount userAccount = new UserAccount();
            userAccount.setCardNumber(request.getCardNumber());
            userAccount.setPin(request.getPin());

            // Encrypt and set balance
            String encryptedBalance = encryptionUtil.encryptBalance(request.getBalance());
            userAccount.setEncryptedBalance(encryptedBalance);
            System.out.println("Balance encrypted successfully");

            // Link to user details
            userAccount.setUserDetails(savedUserDetails);
            savedUserDetails.setUserAccount(userAccount);

            // Save account
            UserAccount savedAccount = userAccountRepository.save(userAccount);
            System.out.println("User account saved successfully");

            System.out.println("=== BACKEND: Account created successfully ===");
            System.out.println("Card: " + savedAccount.getCardNumber());
            System.out.println("User ID: " + savedUserDetails.getId());

            return ResponseEntity.ok(Map.of(
                    "message", "Account created successfully",
                    "cardNumber", savedAccount.getCardNumber(),
                    "userId", savedUserDetails.getId(),
                    "userName", savedUserDetails.getName(),
                    "success", true
            ));

        } catch (Exception e) {
            System.err.println("=== BACKEND ERROR ===");
            System.err.println("Error type: " + e.getClass().getSimpleName());
            System.err.println("Error message: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Error creating account: " + e.getMessage()));
        }
    }

    // Test endpoint
    @GetMapping("/test")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok(Map.of(
                "message", "AccountRegistrationController is working!",
                "timestamp", System.currentTimeMillis(),
                "entityFields", Map.of(
                        "name", "maps to fullName from frontend",
                        "dateOfBirth", "maps to dob from frontend",
                        "age", "calculated from dob",
                        "contactNumber", "maps to phone from frontend",
                        "email", "maps to email from frontend",
                        "address", "maps to address from frontend"
                )
        ));
    }
}