/*
 * KnownWordService.java
 *
 * Created on 2017-11-11
 *

 */

package de.lingling.backend.service;

import java.util.ArrayList;
import java.util.Collection;
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

    private static final Pattern SPLIT_PATTERN = Pattern.compile("[ ,\\.:!]");
    private final KnownWordRepository repository;

    public KnownWordService(final KnownWordRepository repository) {
        this.repository = repository;
    }

    public List<String> extractUnknownWordsFromSentence(final Learner learner, final CharSequence latestSentence) {
        final List<String> knownWords = repository.findAllByLearner(learner).stream()
                                                  .map(w -> w.getWord().getText())
                                                  .collect(Collectors.toList());
        final String[] split = SPLIT_PATTERN.split(latestSentence);
        final List<String> unknownWords = new ArrayList<>();

        for (final String sentenceWord : split) {
            if (!isWordKnown(sentenceWord, knownWords)) {
                unknownWords.add(sentenceWord.toLowerCase());
            }
        }


        return unknownWords;
    }

    private boolean isWordKnown(final String needle, final Iterable<String> haystack) {
        for (final String el : haystack) {
            if (needle.toLowerCase().contains(el.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public void addNewWords(final Collection<Word> newWords, final Learner learner) {
        repository.save(newWords.stream().map(word -> new KnownWord(word, learner)).collect(Collectors.toList()));
    }

    public int countKnownWords(final Learner learner) {
        return repository.findAllByLearner(learner).size();
    }
}
