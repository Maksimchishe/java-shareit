package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepository implements UserStorage {

    private long id;
    private final Map<Long, User> userMap = new HashMap<>();

    private long nextId() {
        return id++;
    }

    @Override
    public List<User> getUsers() {
        return userMap.values().stream().toList();
    }

    @Override
    public User getUserById(long id) {
        if (!userMap.containsKey(id)) {
            throw new NotFoundException("Пользователь не найден.");
        }
        return userMap.get(id);
    }

    @Override
    public User createUser(User user) {
        if (userMap.values().stream()
                .anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            throw new ValidationException("email уже существует.");
        }

        long id = nextId();
        user.setId(id);
        userMap.put(id, user);
        return userMap.get(id);
    }

    @Override
    public User updateUser(User user, long id) {
        User updateUser = getUserById(id);
        if (user.getName() != null) {
            updateUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            if (userMap.values().stream()
                    .anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
                throw new ValidationException("email уже существует.");
            }
            updateUser.setEmail(user.getEmail());
        }
        userMap.put(id, updateUser);
        return userMap.get(id);
    }

    @Override
    public void deleteUser(long id) {
        getUserById(id);
        userMap.remove(id);
    }
}
