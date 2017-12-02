/*
 * LearnerRepository.java
 *
 * Created on 2017-11-11
 *

 */

package de.lingling.backend.repository;

import org.springframework.data.repository.CrudRepository;

import de.lingling.backend.domain.Account;
import de.lingling.backend.domain.Learner;

public interface LearnerRepository extends CrudRepository<Learner, Long> {
    Learner findByAccount(Account account);
}
