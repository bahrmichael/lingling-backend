/*
 * KnownWordService.java
 *
 * Created on 2017-11-11
 *

 */

package de.lingling.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import de.lingling.backend.domain.KnownWord;
import de.lingling.backend.domain.Learner;
import de.lingling.backend.domain.Word;
import de.lingling.backend.repository.KnownWordRepository;

@Component
@Transactional
public class KnownWordService {
    private static final Pattern SPLIT_PATTERN = Pattern.compile("[ ,\\.:]");
    private final KnownWordRepository repository;

    public KnownWordService(final KnownWordRepository repository) {
        this.repository = repository;
    }

    public List<String> extractUnknownWordsFromSentence(final Learner learner, final String latestSentence) {
        final List<String> knownWords = repository.findAllByLearner(learner).stream()
                                                  .map(w -> w.getWord().getText())
                                                  .collect(Collectors.toList());
        final String[] split = SPLIT_PATTERN.split(latestSentence);
        final List<String> unknownWords = new ArrayList<>();
        for (final String sentenceWord : split) {
            for (final String knownWord : knownWords) {
                if (sentenceWord.contains(knownWord)) {
                    if (sentenceWord.toLowerCase().contains(knownWord.toLowerCase())) {
                        unknownWords.add(sentenceWord);
                    }
                    // continue with the next word of the sentence
                    break;
                }
            }
        }

        return unknownWords;
    }

    public void addNewWords(final List<Word> newWords, final Learner learner) {
        repository.save(newWords.stream().map(word -> new KnownWord(word, learner)).collect(Collectors.toList()));
    }

    public int countKnownWords(final Learner learner) {
        return repository.findAllByLearner(learner).size();
    }
}
