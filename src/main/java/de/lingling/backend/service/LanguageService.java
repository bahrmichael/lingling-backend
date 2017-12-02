package de.lingling.backend.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import de.lingling.backend.domain.Language;
import de.lingling.backend.repository.LanguageRepository;

@Component
@Transactional
public class LanguageService {
    private final LanguageRepository repository;

    public LanguageService(final LanguageRepository repository) {
        this.repository = repository;
    }

    public void add(final Language language) {
        repository.save(language);
    }
}
