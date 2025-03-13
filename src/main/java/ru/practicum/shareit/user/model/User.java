package ru.practicum.shareit.user.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * TODO Sprint add-controllers.
 */
@Entity
@Table(name = "users", schema = "public")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "first_name", nullable = false)
    private String name;
    @Column(unique = true)
    private String email;
    @Column(name = "registration_date")
    private Instant registrationDate = Instant.now();
}