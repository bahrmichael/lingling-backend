package de.lingling.backend.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import de.lingling.backend.domain.Action;
import de.lingling.backend.domain.Audit;
import de.lingling.backend.repository.AuditRepository;

@Component
@Transactional
public class AuditService {

    private final AuditRepository repository;

    public AuditService(final AuditRepository repository) {
        this.repository = repository;
    }

    public void addAudit(final String alexaId, final String utterance, final Action action, final String returnedValue) {
        final Audit entity = new Audit(alexaId, utterance, action);
        entity.setReturnedValue(returnedValue);
        repository.save(entity);
    }

    public Audit findLatestForUser(final String alexaId) {
        return repository.findFirstByAlexaIdOrderByTimestampDesc(alexaId);
    }

    public String findLatestSentence(final String alexaId) {
        final Optional<Audit> optional = repository.findFirstByAlexaIdAndActionOrderByTimestampDesc(alexaId, Action.SENTENCE);
        return optional.orElseThrow(this::noSentence).getReturnedValue();
    }

    private IllegalStateException noSentence() {
        return new IllegalStateException("No sentence could be found for the user.");
    }

    public String findLatestFrequencyWord(final String alexaId) {
        return repository.findFirstByAlexaIdAndActionOrderByTimestampDesc(alexaId, Action.FREQUENCY_WORD)
                         .map(Audit::getReturnedValue).orElse(null);
    }
}
