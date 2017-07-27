package ru.rubilnik.bot.data.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by Alexey on 27.07.2017.
 */
@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"userId", "vote_id"})})
@NoArgsConstructor
public class VoteAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter
    @Column(nullable = false)
    private Boolean yes;

    @Getter
    @Column(nullable = false)
    private Long userId;

    @Getter
    @ManyToOne
    @JoinColumn(name = "vote_id", nullable = false)
    private Vote vote;

    public VoteAnswer(Boolean yes, Long userId, Vote vote) {
        this.yes = yes;
        this.userId = userId;
        this.vote = vote;
    }
}
