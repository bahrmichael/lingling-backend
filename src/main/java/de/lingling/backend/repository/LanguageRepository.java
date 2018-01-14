/*
 * LanguageRepository.java
 *
 * Created on 2017-11-11
 *

 */

package de.lingling.backend.repository;

import org.springframework.data.repository.CrudRepository;

import de.lingling.backend.domain.Language;

public interface LanguageRepository extends CrudRepository<Language, Long> {
    Language findOneByLanguageCode(String languageCode);
}
