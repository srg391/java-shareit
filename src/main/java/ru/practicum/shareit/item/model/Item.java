package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;
    @Column(name = "description", nullable = false, length = 1000)
    private String description;
    @Column(name = "is_available", nullable = false)
    private Boolean available;

    @JoinColumn(name = "owner_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    @JoinColumn(name = "request_id")
    private Long requestId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "item")
    private final List<Comment> comments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "item")
    private final List<Comment> bookings = new ArrayList<>();

    public void addComment(Comment comment) {
        comments.add(comment);
    }

}
