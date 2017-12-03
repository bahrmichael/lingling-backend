package de.lingling.backend.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import de.lingling.backend.domain.Account;
import de.lingling.backend.domain.Action;
import de.lingling.backend.domain.Language;
import de.lingling.backend.domain.Learner;
import de.lingling.backend.domain.Sentence;
import de.lingling.backend.repository.AuditRepository;
import de.lingling.backend.repository.SentenceRepository;

@Component
@Transactional
public class SentenceService {
    private final SentenceRepository repository;
    private final AuditRepository auditRepository;
    private final AccountService accountService;

    public SentenceService(final SentenceRepository repository,
            final AuditRepository auditRepository,
            final AccountService accountService) {
        this.repository = repository;
        this.auditRepository = auditRepository;
        this.accountService = accountService;
    }

    public Sentence getRandomSentence(final Language src, final Language dst, final Learner learner) {
        // todo: throw dedicated exception instead?
        // todo: filter for missing words by learner
        return repository.findAllByLanguageSrcAndAndLanguageDst(src, dst).stream().findAny().orElse(null);
    }

    public Sentence getRecentSentence(final Account account) {
        // todo: throw dedicated exception instead?
        return auditRepository.findFirstByAlexaIdAndActionOrderByTimestampDesc(account.getAlexaId(), Action.SENTENCE)
                              .map(audit -> repository.findByTextDst(audit.getReturnedValue()))
                              .orElse(null);
    }

    public Sentence getRecentSentence(final String alexaId) {
        return getRecentSentence(accountService.findAccount(alexaId));
    }
}
