/*
 * AccountService.java
 *
 * Created on 2017-11-11
 *

 */

package de.lingling.backend.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import de.lingling.backend.domain.Account;
import de.lingling.backend.repository.AccountRepository;
import de.lingling.backend.repository.LanguageRepository;

@Component
@Transactional
public class AccountService {

    private final AccountRepository repository;
    private final LanguageRepository languageRepository;

    public AccountService(final AccountRepository repository,
                          final LanguageRepository languageRepository) {
        this.repository = repository;
        this.languageRepository = languageRepository;
    }

    public Account createAccount(final String alexaId, final String languageIsoCode) {
        final Account account = new Account();
        account.setAlexaId(alexaId);
        account.setLanguageSrc(languageRepository.findByIsoCode(languageIsoCode));
        repository.save(account);
        return account;
    }

    public Account findAccount(final String alexaId) {
        return repository.findByAlexaId(alexaId);
    }
}
