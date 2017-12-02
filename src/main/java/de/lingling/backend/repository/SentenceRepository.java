/*
 * SentenceRepository.java
 *
 * Created on 2017-11-11
 *

 */

package de.lingling.backend.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.lingling.backend.domain.Language;
import de.lingling.backend.domain.Sentence;

public interface SentenceRepository extends CrudRepository<Sentence, Long> {
    List<Sentence> findAllByLanguageSrcAndAndLanguageDst(Language src, Language dst);
    Sentence findByTextDst(String textDst);
}
