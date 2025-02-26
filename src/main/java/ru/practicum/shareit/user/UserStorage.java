package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {

    List<User> getUsers();

    User getUserById(long id);

    User createUser(User user);

    User updateUser(User user, long id);

    void deleteUser(long id);
}
