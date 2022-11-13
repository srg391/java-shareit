package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "Название не соотвествует!")
    private String name;

    private String description;

    private Boolean available;

    @JoinColumn(name = "owner_id")
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
