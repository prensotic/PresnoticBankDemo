package repositories;

import models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private final Map<Long, User> users = new HashMap<>();

    public List<User> getAllUsers(){ return new ArrayList<>(users.values()); }

    public User getUserById(long id){ return users.get(id); }

    public void saveUser(User user){ users.put(user.getId(), user); }

    public void deleteUser(long id){ users.remove(id); }
}