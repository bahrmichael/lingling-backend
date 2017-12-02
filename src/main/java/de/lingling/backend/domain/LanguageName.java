/*
 * LanguageName.java
 *
 * Created on 2017-11-11
 *

 */

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
public class LanguageName implements Serializable {

    private static final long serialVersionUID = -3409758029040513120L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = Columns.LANGUAGE_SRC_ID)
    private Language languageSrc;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = Columns.LANGUAGE_DST_ID)
    private Language languageDst;

    @Column(nullable = false)
    private String name;

    public Long getId() {
        return id;
    }

    public Language getLanguageSrc() {
        return languageSrc;
    }

    public void setLanguageSrc(final Language languageSrc) {
        this.languageSrc = languageSrc;
    }

    public Language getLanguageDst() {
        return languageDst;
    }

    public void setLanguageDst(final Language languageDst) {
        this.languageDst = languageDst;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
