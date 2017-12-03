/*
 * WordServiceTest.java
 *
 * Created on 2017-12-03
 *
 * Copyright (C) 2017 Volkswagen AG, All rights reserved.
 */

package de.lingling.backend.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.lingling.backend.domain.KnownWord;
import de.lingling.backend.domain.Language;
import de.lingling.backend.domain.Learner;
import de.lingling.backend.domain.Word;
import de.lingling.backend.repository.KnownWordRepository;
import de.lingling.backend.repository.WordRepository;

public class WordServiceTest {

    private final WordRepository wordRepo = mock(WordRepository.class);
    private final KnownWordRepository knownWordRepo = mock(KnownWordRepository.class);
    private final LearnerService learnerService = mock(LearnerService.class);
    private final WordService sut = new WordService(wordRepo, knownWordRepo, learnerService);

    @Test
    public void findWord() {
        final Language language = new Language();
        final String aWord = "aWord";

        when(wordRepo.findWord(aWord, language)).thenReturn(new Word(aWord));

        final Word word = sut.findWord(aWord, language);
        assertEquals(aWord, word.getText());
    }

    @Test
    public void findWords() {
        final Language language = new Language();
        final String aWord = "aWord";

        when(wordRepo.findWord(aWord, language)).thenReturn(new Word(aWord));

        final List<Word> words = sut.findWords(Collections.singletonList(aWord), language);
        assertEquals(1, words.size());
        assertEquals(aWord, words.get(0).getText());
    }

    @Test
    public void findWords_noResult() {
        final Language language = new Language();
        final String aWord = "aWord";

        when(wordRepo.findWord(aWord, language)).thenReturn(null);

        final List<Word> words = sut.findWords(Collections.singletonList(aWord), language);
        assertEquals(0, words.size());
    }

    @Test
    public void getNextFrequencyWord() {
        final Learner learner = new Learner();
        final Word aWord = new Word("aWord");
        aWord.setId(1L);
        final KnownWord knownWord = new KnownWord(aWord, learner);
        when(knownWordRepo.findAllByLearner(learner)).thenReturn(Collections.singletonList(knownWord));
        when(wordRepo.findTop10ByIdNotInOrderByFrequencyDesc(Collections.singletonList(1L))).thenReturn(Stream.of(aWord));

        final Word nextFrequencyWord = sut.getNextFrequencyWord(learner);

        assertEquals("aWord", nextFrequencyWord.getText());
        assertEquals(1, nextFrequencyWord.getId().intValue());
    }

    @Test
    public void getNextFrequencyWord_withNoKnownWords() {
        final Learner learner = new Learner();
        final Word aWord = new Word("aWord");
        aWord.setId(1L);
        when(knownWordRepo.findAllByLearner(learner)).thenReturn(Collections.emptyList());
        when(wordRepo.findTop10ByIdNotInOrderByFrequencyDesc(Collections.emptyList())).thenReturn(Stream.of(aWord));

        final Word nextFrequencyWord = sut.getNextFrequencyWord(learner);

        assertEquals("aWord", nextFrequencyWord.getText());
        assertEquals(1, nextFrequencyWord.getId().intValue());
    }

    @Test
    public void getNextFrequencyWord_withAllKnownWords() {
        final Learner learner = new Learner();
        final Word aWord = new Word("aWord");
        aWord.setId(1L);
        final KnownWord knownWord = new KnownWord(aWord, learner);
        when(knownWordRepo.findAllByLearner(learner)).thenReturn(Collections.singletonList(knownWord));
        when(wordRepo.findTop10ByIdNotInOrderByFrequencyDesc(anyList())).thenReturn(Stream.empty());

        final Word nextFrequencyWord = sut.getNextFrequencyWord(learner);
        assertNull(nextFrequencyWord);
    }

    // todo: test that there is some variance -> not always the first top prio word
}
