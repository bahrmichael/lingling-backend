package de.lingling.backend.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "LEARNER")
@Entity
public class Learner implements Serializable {

    private static final long serialVersionUID = -2125885597458254676L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = Columns.LEARNER_ACCOUNT_ID)
    private Account account;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = Columns.LEARNER_LANGUAGE_DST_ID)
    private Language languageDst;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(final Account account) {
        this.account = account;
    }

    public Language getLanguageDst() {
        return languageDst;
    }

    public void setLanguageDst(final Language languageDst) {
        this.languageDst = languageDst;
    }
}
