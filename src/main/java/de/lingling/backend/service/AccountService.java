/*
 * AccountService.java
 *
 * Created on 2017-11-11
 *

 */

package de.lingling.backend.service;

import org.springframework.stereotype.Component;

import de.lingling.backend.domain.Account;
import de.lingling.backend.domain.Language;
import de.lingling.backend.repository.AccountRepository;
import de.lingling.backend.repository.LanguageRepository;

@Component
public class AccountService {

    private final AccountRepository repository;
    private final LanguageRepository languageRepository;

    public AccountService(final AccountRepository repository,
                          final LanguageRepository languageRepository) {
        this.repository = repository;
        this.languageRepository = languageRepository;
    }

    public void createAccount(final String alexaId, final String languageIsoCode) {
        final Language language = languageRepository.findOneByLanguageCode(languageIsoCode);

        final Account account = new Account();
        account.setAlexaId(alexaId);
        account.setLanguageSrc(language);
        repository.save(account);
    }

    public Account findAccount(final String alexaId) {
        return repository.findOneByAlexaId(alexaId);
    }
}
