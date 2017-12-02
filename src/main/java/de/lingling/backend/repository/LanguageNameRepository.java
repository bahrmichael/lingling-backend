/*
 * LanguageNameRepository.java
 *
 * Created on 2017-11-11
 *

 */

package de.lingling.backend.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.lingling.backend.domain.Language;
import de.lingling.backend.domain.LanguageName;

public interface LanguageNameRepository extends CrudRepository<LanguageName, Long> {
    List<LanguageName> findAllByLanguageSrc(Language src);

    LanguageName findByName(String languageName);
}
