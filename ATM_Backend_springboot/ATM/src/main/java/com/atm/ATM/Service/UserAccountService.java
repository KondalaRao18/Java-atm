package com.atm.ATM.Service;

import com.atm.ATM.Entity.UserAccount;
import com.atm.ATM.Repository.UserAccountRepo;
import com.atm.ATM.Util.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserAccountService {
    @Autowired
    private UserAccountRepo accountRepo;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private EncryptionUtil encryptionUtil;

    public UserAccount getByCardNumber(String cardNumber) {
        UserAccount account = accountRepo.findById(cardNumber).orElse(null);
        if (account != null) {
            // Decrypt balance when retrieving from database
            double decryptedBalance = encryptionUtil.decryptBalance(account.getEncryptedBalance());
            account.setBalance(decryptedBalance);
        }
        return account;
    }

    public UserAccount register(UserAccount accounts) {
        // Encrypt balance before saving
        String encryptedBalance = encryptionUtil.encryptBalance(accounts.getBalance());
        accounts.setEncryptedBalance(encryptedBalance);
        return accountRepo.save(accounts);
    }

    public UserAccount authenticate(String cardNumber, String pin) {
        Optional<UserAccount> optional = accountRepo.findByCardNumberAndPin(cardNumber, pin);
        if (optional.isPresent()) {
            UserAccount account = optional.get();
            // Decrypt balance for authenticated user
            double decryptedBalance = encryptionUtil.decryptBalance(account.getEncryptedBalance());
            account.setBalance(decryptedBalance);
            return account;
        }
        return null;
    }

    public String deposit(String cardNumber, double amount) {
        UserAccount acc = getByCardNumber(cardNumber);
        if (acc == null) return "Account not found";

        // Update balance
        double newBalance = acc.getBalance() + amount;
        acc.setBalance(newBalance);

        // Encrypt new balance before saving
        String encryptedBalance = encryptionUtil.encryptBalance(newBalance);
        acc.setEncryptedBalance(encryptedBalance);

        accountRepo.save(acc);
        transactionService.logTransaction(cardNumber, "DEPOSIT", amount, "Amount deposited");
        return "Amount Deposited";
    }

    public String withdraw(String cardNumber, double amount) {
        UserAccount acc = getByCardNumber(cardNumber);
        if (acc == null) return "Account not found";
        if (acc.getBalance() < amount) return "Insufficient Balance";

        // Update balance
        double newBalance = acc.getBalance() - amount;
        acc.setBalance(newBalance);

        // Encrypt new balance before saving
        String encryptedBalance = encryptionUtil.encryptBalance(newBalance);
        acc.setEncryptedBalance(encryptedBalance);

        accountRepo.save(acc);
        transactionService.logTransaction(cardNumber, "WITHDRAW", amount, "Amount withdrawn");
        return "Amount Withdrawn";
    }

    public String changePIN(String cardNumber, String newPin) {
        Optional<UserAccount> optionalAccount = accountRepo.findByCardNumber(cardNumber);
        if (optionalAccount.isEmpty()) {
            return "Account not found";
        }
        UserAccount account = optionalAccount.get();
        account.setPin(newPin);
        accountRepo.save(account);
        return "PIN changed successfully";
    }

    public double getBalance(String cardNumber) {
        return accountRepo.findByCardNumber(cardNumber)
                .map(UserAccount::getBalance)
                .orElseThrow(() -> new RuntimeException("Card number not found: " + cardNumber));
    }

//    public UserAccount getByCardNumber(String cardNumber) {
//        return accountRepo.findById(cardNumber).orElse(null);
//    }
//
//    public UserAccount register(UserAccount accounts) {
//        return accountRepo.save(accounts);
//    }
//
//    public UserAccount authenticate(String cardNumber, String pin) {
//        Optional<UserAccount> optional = accountRepo.findByCardNumberAndPin(cardNumber, pin);
//        return optional.orElse(null);
//    }
//
//    public String deposit(String cardNumber, double amount) {
//        UserAccount acc = getByCardNumber(cardNumber);
//        if (acc == null) return "Account not found";
//        acc.setBalance(acc.getBalance() + amount);
//        accountRepo.save(acc);
//        transactionService.logTransaction(cardNumber, "DEPOSIT", amount, "Amount deposited");
//        return "Amount Deposited";
//    }
//
//    public String withdraw(String cardNumber, double amount) {
//        UserAccount acc = getByCardNumber(cardNumber);
//        if (acc == null) return "Account not found";
//        if (acc.getBalance() < amount) return "Insufficient Balance";
//        acc.setBalance(acc.getBalance() - amount);
//        accountRepo.save(acc);
//        transactionService.logTransaction(cardNumber, "WITHDRAW", amount, "Amount withdrawn");
//        return "Amount Withdrawn";
//    }
//
//    public String changePIN(String cardNumber, String newPin) {
//        UserAccount acc = getByCardNumber(cardNumber);
//        if (acc != null) {
//            acc.setPin(newPin);
//            accountRepo.save(acc);
//            return "PIN changed successfully";
//        }
//        return null;
//    }
//
//    public double getBalance(String cardNumber) {
//        UserAccount acc = getByCardNumber(cardNumber);
//        return acc != null ? acc.getBalance() : 0.0;
//    }

}
