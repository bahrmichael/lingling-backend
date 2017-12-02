/*
 * UtteranceRepository.java
 *
 * Created on 2017-11-11
 *

 */

package de.lingling.backend.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import de.lingling.backend.domain.Action;
import de.lingling.backend.domain.Audit;

public interface AuditRepository extends CrudRepository<Audit, Long> {
    Audit findFirstByAlexaIdOrderByTimestampDesc(String alexaId);

    Optional<Audit> findFirstByAlexaIdAndActionOrderByTimestampDesc(String alexaId, Action action);
}
