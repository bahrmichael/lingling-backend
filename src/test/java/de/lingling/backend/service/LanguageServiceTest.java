package de.lingling.backend.service;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.lingling.backend.domain.Language;
import de.lingling.backend.repository.LanguageRepository;

public class LanguageServiceTest {

    private final LanguageRepository repo = mock(LanguageRepository.class);
    private final LanguageService sut = new LanguageService(repo);

    @Test
    public void add() {
        final Language language = new Language();

        when(repo.save(language)).thenReturn(null);

        sut.add(language);

        verify(repo).save(language);
    }
}
