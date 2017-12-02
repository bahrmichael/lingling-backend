/*
 * WordService.java
 *
 * Created on 2017-11-11
 *

 */

package de.lingling.backend.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import de.lingling.backend.domain.KnownWord;
import de.lingling.backend.domain.Language;
import de.lingling.backend.domain.Learner;
import de.lingling.backend.domain.Word;
import de.lingling.backend.repository.KnownWordRepository;
import de.lingling.backend.repository.WordRepository;

@Component
@Transactional
public class WordService {
    private final WordRepository repository;
    private final KnownWordRepository knownWordRepository;

    public WordService(final WordRepository repository,
                       final KnownWordRepository knownWordRepository) {
        this.repository = repository;
        this.knownWordRepository = knownWordRepository;
    }

    public Word findWord(final String unknownWord, final Language language) {
        return findWords(Collections.singletonList(unknownWord), language).get(0);
    }

    public List<Word> findWords(final List<String> unknownWords, final Language language) {
        return unknownWords.stream().map(s -> repository.findWord(s, language)).collect(Collectors.toList());
    }

    public Word getNextFrequencyWord(final Learner learner) {
        final List<Long> knownWordIds = knownWordRepository.findAllByLearner(learner).stream()
                                                           .map(KnownWord::getWord).map(Word::getId)
                                                           .collect(Collectors.toList());
        return repository.findTop10ByIdNotInOrderByFrequencyDesc(knownWordIds).findAny().orElse(null);
    }
}
