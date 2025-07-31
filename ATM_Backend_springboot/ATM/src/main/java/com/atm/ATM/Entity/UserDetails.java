package com.atm.ATM.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_details")
@Getter
@Setter
@NoArgsConstructor
public class UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String name;
    private String dateOfBirth;
    private String age;
    private String contactNumber;
    private String email;
    private String address;

    @OneToOne
    @JoinColumn(name = "card_number", referencedColumnName = "card_number", unique = true)
    @JsonBackReference
    private UserAccount userAccount;

}
