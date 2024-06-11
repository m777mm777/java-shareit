package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.question.model.Question;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    private Boolean available;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner", referencedColumnName = "id")
    private User owner;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    private Question question;

}
