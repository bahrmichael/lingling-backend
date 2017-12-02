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
public class Sentence implements Serializable {

    private static final long serialVersionUID = 7962287473266845546L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = Columns.SENTENCE_LANGUAGE_SRC_ID)
    private Language languageSrc;

    @Column(nullable = false)
    private String textSrc;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = Columns.SENTENCE_LANGUAGE_DST_ID)
    private Language languageDst;

    @Column(nullable = false)
    private String textDst;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Language getLanguageSrc() {
        return languageSrc;
    }

    public void setLanguageSrc(final Language languageSrc) {
        this.languageSrc = languageSrc;
    }

    public String getTextSrc() {
        return textSrc;
    }

    public void setTextSrc(final String textSrc) {
        this.textSrc = textSrc;
    }

    public Language getLanguageDst() {
        return languageDst;
    }

    public void setLanguageDst(final Language languageDst) {
        this.languageDst = languageDst;
    }

    public String getTextDst() {
        return textDst;
    }

    public void setTextDst(final String textDst) {
        this.textDst = textDst;
    }
}
