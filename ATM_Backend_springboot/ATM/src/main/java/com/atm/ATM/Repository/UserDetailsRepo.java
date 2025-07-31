package com.atm.ATM.Repository;

import com.atm.ATM.Entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserDetailsRepo extends JpaRepository<UserDetails , Long> {
    @Query("SELECT ud FROM UserDetails ud WHERE ud.userAccount.cardNumber = :cardNumber")
    Optional<UserDetails> findByCardNumber(@Param("cardNumber") String cardNumber);
}
