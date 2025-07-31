package com.atm.ATM.Service;

import com.atm.ATM.Entity.UserDetails;
import com.atm.ATM.Repository.UserDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsService {
    @Autowired
    private UserDetailsRepo detailsRepo;

    public UserDetails saveDetails(UserDetails details) {
        return detailsRepo.save(details);
    }

    public UserDetails getById(Long id) {
        return detailsRepo.findById(id).orElse(null);
    }

    public List<UserDetails> getAll() {
        return detailsRepo.findAll();
    }

//    public String getCardNumberByUserId(Long id) {
//        return detailsRepo.findById(id)
//                .map(user -> {
//                    if (user.getUserAccount() != null) {
//                        return user.getUserAccount().getCardNumber();
//                    } else {
//                        throw new RuntimeException("UserAccount not linked for user id: " + id);
//                    }
//                })
//                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
//    }

    public UserDetails getDetailsByCardNumber(String cardNumber) {
        Optional<UserDetails> user = detailsRepo.findByCardNumber(cardNumber);
        return user.orElse(null);
    }

    public UserDetails updateDetailsByCardNumber(String cardNumber, UserDetails updatedDetails) {
        return detailsRepo.findByCardNumber(cardNumber).map(existing -> {
            existing.setName(updatedDetails.getName());
            existing.setEmail(updatedDetails.getEmail());
            existing.setAddress(updatedDetails.getAddress());
            existing.setAge(updatedDetails.getAge());
            existing.setContactNumber(updatedDetails.getContactNumber());
            existing.setDateOfBirth(updatedDetails.getDateOfBirth());
            return detailsRepo.save(existing);
        }).orElse(null);
    }

//    private UserDetailsRepo userDetailsRepo;
//
//    public List<UserDetails> saveUser(List<UserDetails> userDetails) {
//        return userDetailsRepo.saveAll(userDetails);
//    }
//
//    public List<UserDetails> getAllUsers() {
//        return userDetailsRepo.findAll();
//    }
}
