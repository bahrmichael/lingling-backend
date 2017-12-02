package de.lingling.backend.service;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.lingling.backend.domain.Account;
import de.lingling.backend.domain.Language;
import de.lingling.backend.domain.Learner;
import de.lingling.backend.repository.LearnerRepository;

public class LearnerServiceTest {

    private final LearnerRepository repo = mock(LearnerRepository.class);
    private final LearnerService sut = new LearnerService(repo);

    @Test
    public void addLearner() {
        final Account account = new Account();
        final Language language = new Language();

        final ArgumentCaptor<Learner> captor = ArgumentCaptor.forClass(Learner.class);
        when(repo.save(captor.capture())).then(returnsFirstArg());

        sut.addLearner(account, language);

        verify(repo).save(any(Learner.class));
        final Learner result = captor.getValue();
        assertEquals(account, result.getAccount());
        assertEquals(language, result.getLanguageDst());
    }

    @Test
    public void findLatestLearnerForAccount() {
        final Account account = new Account();
        final Learner expected = new Learner();

        when(repo.findByAccount(account)).thenReturn(expected);

        final Learner result = sut.findLatestLearnerForAccount(account);

        assertEquals(expected, result);
    }
}
