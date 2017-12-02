/*
 * KnownWordRepository.java
 *
 * Created on 2017-11-11
 *

 */

package de.lingling.backend.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.lingling.backend.domain.KnownWord;
import de.lingling.backend.domain.Learner;

public interface KnownWordRepository extends CrudRepository<KnownWord, Long> {
    List<KnownWord> findAllByLearner(Learner learner);
}
