package de.lingling.backend.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Word implements Serializable {

    private static final long serialVersionUID = -3214848959612491606L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = Columns.WORD_LANGUAGE_ID)
    private Language language;

    @Column(nullable = false, name = Columns.WORD_TEXT)
    private String text;

    @Column(nullable = false, name = Columns.WORD_FREQUENCY)
    private Long frequency;

    public Word() {
    }

    public Word(final String text) {
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(final Language language) {
        this.language = language;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public Long getFrequency() {
        return frequency;
    }

    public void setFrequency(final Long frequency) {
        this.frequency = frequency;
    }
}
