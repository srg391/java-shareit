package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "item_requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @JoinColumn(name = "requester_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User requester;

    @Column(name = "date_of_creation")
    private LocalDateTime dateOfCreation;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "requestId")
    private final List<Item> items = new ArrayList<>();
}
