package ru.rubilnik.bot.data.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Alexey on 21.07.2017.
 */
@Entity
@NoArgsConstructor
public class Vote {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter
    @Column(nullable = false)
    private Long chatId;

    @Getter
    @Column(nullable = false)
    private String question;

    @Getter
    @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<VoteAnswer> answers;


    public Vote(Long chatId, String question) {
        this.chatId = chatId;
        this.question = question;
    }
}
