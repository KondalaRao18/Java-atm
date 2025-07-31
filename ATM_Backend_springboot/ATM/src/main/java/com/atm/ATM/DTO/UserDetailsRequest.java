package com.atm.ATM.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailsRequest {
    private String fullName;
    private String dob;
    private String gender;
    private String email;
    private String phone;
    private String address;

    @Override
    public String toString() {
        return "UserDetailsRequest{" +
                "fullName='" + fullName + '\'' +
                ", dob='" + dob + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}