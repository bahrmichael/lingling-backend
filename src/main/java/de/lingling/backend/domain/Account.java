package de.lingling.backend.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Account implements Serializable {

    private static final long serialVersionUID = 6351442974927682866L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, name = Columns.ACCOUNT_ALEXA_ID)
    private String alexaId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = Columns.ACCOUNT_LANGUAGE_SRC)
    private Language languageSrc;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = Columns.LEARNER_ID)
    private Set<Learner> learners;

    public Long getId() {
        return id;
    }

    public String getAlexaId() {
        return alexaId;
    }

    public void setAlexaId(final String alexaId) {
        this.alexaId = alexaId;
    }

    public Language getLanguageSrc() {
        return languageSrc;
    }

    public void setLanguageSrc(final Language languageSrc) {
        this.languageSrc = languageSrc;
    }

    public Set<Learner> getLearners() {
        return learners;
    }

    public void setLearners(final Set<Learner> learners) {
        this.learners = learners;
    }
}
