package services;

import enums.Gender;
import models.User;
import repositories.UserRepository;

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
    private final UserRepository userRepository;

    public AuthService(UserService userService, Random random, UserRepository userRepository){
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public boolean login(String phoneNumber, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if(password == null || password.isBlank())
            throw new IllegalArgumentException("Пароль должен быть указан.");
        if(phoneNumber == null || phoneNumber.isBlank())
            throw new IllegalArgumentException("Номер телефона должен быть указан.");

        User user = userRepository.getUserByPhoneNumber(phoneNumber);

        if (user == null) {
            throw new IllegalArgumentException("Пользователь с таким номером телефона не существует.");
        }
        return PasswordService.verifyPassword(password, user.getPasswordSalt(), user.getPasswordHash());
    }

    public boolean registration(String firstName, String lastName, LocalDate dateOfBirth, Gender gender, String phoneNumber, String password) {
        try{
            byte[] passwordSalt = PasswordService.generateSalt();
            String passwordHash = PasswordService.hashPassword(password, passwordSalt);
            User newUser = new User(firstName, lastName, dateOfBirth, gender, phoneNumber, passwordHash, passwordSalt);
            userService.createUser(newUser);
        }
        catch(IllegalArgumentException | NoSuchAlgorithmException | InvalidKeySpecException e){
            System.out.println("Ошибка при регистрации: " + e.getMessage());
            return false;
        }
        return true;
    }
}
