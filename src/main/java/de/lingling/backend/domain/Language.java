package de.lingling.backend.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Language implements Serializable {

    private static final long serialVersionUID = -7100272813995450835L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String languageCode;

    public Language() {
    }

    public Language(final String languageCode) {
        this.languageCode = languageCode;
    }

    public Long getId() {
        return id;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(final String languageCode) {
        this.languageCode = languageCode;
    }
}
