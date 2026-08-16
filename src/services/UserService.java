package services;

import exceptions.UserNotFoundException;
import models.User;
import repositories.UserRepository;

import java.util.List;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers(){ return userRepository.getAllUsers(); }

    public User getUserById(long id) {
        if(id <= 0)
            throw new IllegalArgumentException("ID пользователя должен быть положительным.");

        User user = userRepository.getUserById(id);

        if(user == null)
            throw new UserNotFoundException("Пользователь с ID: " + id + " не найден.");
        return user;
    }

    public void createUser(User user){
        if(user == null)
            throw new IllegalArgumentException("Пользователь не может быть null.");
        userRepository.saveUser(user);
    }

    public void changeUserFirstName(long id, String firstName){
        if(firstName == null || firstName.isBlank())
            throw new IllegalArgumentException("Имя пользователя должно быть заполнено.");

        User user = getUserById(id);
        user.setFirstName(firstName);
    }

    public void changeUserLastName(long id, String lastName){
        if(lastName == null || lastName.isBlank())
            throw new IllegalArgumentException("Фамилия пользователя должно быть заполнено.");

        User user = getUserById(id);
        user.setLastName(lastName);
    }

    public void deleteUser(long id){
        getUserById(id); // Для проверки, есть ли пользователь с этим Id
        userRepository.deleteUser(id);
    }
}