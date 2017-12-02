package de.lingling.backend.service;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.lingling.backend.domain.Account;
import de.lingling.backend.domain.Language;
import de.lingling.backend.repository.AccountRepository;
import de.lingling.backend.repository.LanguageRepository;

public class AccountServiceTest {

    private static final String LANG_CODE = "en-US";
    private static final String ALEXA_ID = "alexaId";
    private final AccountRepository accountRepo = mock(AccountRepository.class);
    private final LanguageRepository langRepo = mock(LanguageRepository.class);
    private final AccountService sut = new AccountService(accountRepo, langRepo);

    @Test
    public void createAccount() {
        final Language language = new Language();
        when(langRepo.findByLanguageCode(LANG_CODE)).thenReturn(language);

        final ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        when(accountRepo.save(captor.capture())).thenReturn(null);

        sut.createAccount(ALEXA_ID, LANG_CODE);

        verify(langRepo).findByLanguageCode(LANG_CODE);
        verify(accountRepo).save(any(Account.class));
        assertEquals(ALEXA_ID, captor.getValue().getAlexaId());
        assertEquals(language, captor.getValue().getLanguageSrc());
        assertNull(captor.getValue().getLearners());
    }

    @Test
    public void findAccount() {
        final Account account = new Account();
        when(accountRepo.findByAlexaId(ALEXA_ID)).thenReturn(account);

        final Account result = sut.findAccount(ALEXA_ID);

        assertEquals(account, result);
    }
}
