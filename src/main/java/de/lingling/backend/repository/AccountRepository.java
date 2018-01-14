/*
 * AccountRepository.java
 *
 * Created on 2017-11-11
 *

 */

package de.lingling.backend.repository;

import org.springframework.data.repository.CrudRepository;

import de.lingling.backend.domain.Account;

public interface AccountRepository extends CrudRepository<Account, Long> {
    Account findOneByAlexaId(String alexaId);
}
