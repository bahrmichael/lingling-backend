/*
 * WordRepository.java
 *
 * Created on 2017-11-11
 *

 */

package de.lingling.backend.repository;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import de.lingling.backend.domain.Language;
import de.lingling.backend.domain.Word;

public interface WordRepository extends CrudRepository<Word, Long> {

    @Query("select w from Word w where w.text = ?1 and w.language = ?2 ")
    Word findWord(String text, Language language);

    Stream<Word> findTop10ByIdNotInOrderByFrequencyDesc(List<Long> ids);
}
