package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    default User getUserById(long id) {
        return findById(id).orElseThrow(() -> new NotFoundException("User не найден."));
    }
}