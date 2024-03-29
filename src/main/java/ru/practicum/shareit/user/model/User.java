package ru.practicum.shareit.user.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 512)
    @Email
    private String email;

}
