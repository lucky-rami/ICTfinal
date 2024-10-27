package com.ict.finalproject.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    private String userid;
    private String userpwd;
    private String adminid;
    private String adminpwd;
}
