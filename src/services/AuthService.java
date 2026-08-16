package services;

import enums.Gender;
import models.User;
import java.time.LocalDate;
import java.util.Random;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class AuthService {
    private final UserService userService;

    public AuthService(UserService userService, Random random){
        this.userService = userService;
    }

    public boolean login(){
        return true;
    }

    public boolean registration(String firstName, String lastName, LocalDate dateOfBirth, Gender gender, String phoneNumber, String password){



        return true;
    }
}
