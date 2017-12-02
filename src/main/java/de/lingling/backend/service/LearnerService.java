/*
 * LearnerService.java
 *
 * Created on 2017-11-11
 *

 */

package de.lingling.backend.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import de.lingling.backend.domain.Account;
import de.lingling.backend.domain.Language;
import de.lingling.backend.domain.Learner;
import de.lingling.backend.repository.LearnerRepository;

@Component
@Transactional
public class LearnerService {
    private final LearnerRepository repository;

    public LearnerService(final LearnerRepository repository) {
        this.repository = repository;
    }

    public Learner addLearner(final Account account, final Language language) {
        final Learner learner = new Learner();
        learner.setAccount(account);
        learner.setLanguageDst(language);
        return repository.save(learner);
    }

    public Learner findLatestLearnerForAccount(final Account account) {
        return repository.findByAccount(account);
    }
}
