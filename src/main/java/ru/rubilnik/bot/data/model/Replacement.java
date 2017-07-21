package ru.rubilnik.bot.data.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by Alexey on 21.07.2017.
 */
@Entity
@Table(indexes = {@Index(name = "REPLACEMENT_INDEX_0", columnList = "fromWord")})
@NoArgsConstructor
public class Replacement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter
    @Column(nullable = false, unique = true)
    private String fromWord;

    @Getter
    @Setter
    @Column(nullable = false)
    private String toWord;


    public Replacement(String fromWord, String toWord) {
        this.fromWord = fromWord;
        this.toWord = toWord;
    }
}
