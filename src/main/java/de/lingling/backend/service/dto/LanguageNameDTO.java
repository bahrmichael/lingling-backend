/*
 * LanguageNameDTO.java
 *
 * Created on 2017-11-12
 *

 */

package de.lingling.backend.service.dto;

public class LanguageNameDTO {
    private LanguageDTO languageSrc;
    private LanguageDTO languageDst;
    private String name;

    public void setLanguageSrc(final LanguageDTO languageSrc) {
        this.languageSrc = languageSrc;
    }

    public LanguageDTO getLanguageSrc() {
        return languageSrc;
    }

    public void setLanguageDst(final LanguageDTO languageDst) {
        this.languageDst = languageDst;
    }

    public LanguageDTO getLanguageDst() {
        return languageDst;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
