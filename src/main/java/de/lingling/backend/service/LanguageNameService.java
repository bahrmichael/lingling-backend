package de.lingling.backend.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import de.lingling.backend.domain.Language;
import de.lingling.backend.domain.LanguageName;
import de.lingling.backend.repository.LanguageNameRepository;
import de.lingling.backend.repository.LanguageRepository;

@Component
@Transactional
public class LanguageNameService {
    private final LanguageNameRepository repository;
    private final LanguageRepository languageRepository;

    public LanguageNameService(final LanguageNameRepository repository,
                               final LanguageRepository languageRepository) {
        this.repository = repository;
        this.languageRepository = languageRepository;
    }

    /* language must be in the en-US format */
    public List<LanguageName> findPossibleDstLanguages(final String languageCode) {
        // todo: consider wildcard like en-*
        final Language srcLanguage = languageRepository.findByLanguageCode(languageCode);
        return repository.findAllByLanguageSrc(srcLanguage);
    }

    public Language findLanguage(final String languageName) {
        return repository.findByName(languageName).getLanguageDst();
    }

    public void add(final LanguageName language) {
        final Language src = languageRepository.findByLanguageCode(language.getLanguageSrc().getLanguageCode());
        final Language dst = languageRepository.findByLanguageCode(language.getLanguageDst().getLanguageCode());
        language.setLanguageSrc(src);
        language.setLanguageDst(dst);
        repository.save(language);
    }
}
