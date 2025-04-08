package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "items", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    @Column(name = "is_available")
    private boolean available;
    @Column(name = "owner_id")
    private long owner;
    @Column(name = "request_id")
    private long request;
    @Column(name = "registration_date")
    private LocalDateTime registrationDate = LocalDateTime.now();
}
