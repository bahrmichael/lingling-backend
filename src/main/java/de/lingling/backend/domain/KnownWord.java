/*
 * KnownWord.java
 *
 * Created on 2017-11-11
 *

 */

package de.lingling.backend.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class KnownWord implements Serializable {

    private static final long serialVersionUID = 70746521153282515L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = Columns.KNOWN_WORD_WORD_ID)
    private Word word;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = Columns.KNOWN_WORD_LEARNER_ID)
    private Learner learner;

    public KnownWord() {
    }

    public KnownWord(final Word word, final Learner learner) {
        this.word = word;
        this.learner = learner;
    }

    public Long getId() {
        return id;
    }

    public Word getWord() {
        return word;
    }

    public void setWord(final Word word) {
        this.word = word;
    }

    public Learner getLearner() {
        return learner;
    }

    public void setLearner(final Learner learner) {
        this.learner = learner;
    }
}
