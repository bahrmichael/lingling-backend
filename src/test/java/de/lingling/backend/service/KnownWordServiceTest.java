package de.lingling.backend.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.lingling.backend.domain.KnownWord;
import de.lingling.backend.domain.Learner;
import de.lingling.backend.domain.Word;
import de.lingling.backend.repository.KnownWordRepository;

public class KnownWordServiceTest {

    private final KnownWordRepository repo = mock(KnownWordRepository.class);
    private final KnownWordService sut = new KnownWordService(repo);

    @Test
    public void extractUnknownWordsFromSentence_oneNewWord() {
        final Learner learner = new Learner();
        final KnownWord knownWord = new KnownWord();
        knownWord.setWord(new Word("hello"));
        when(repo.findAllByLearner(learner)).thenReturn(Collections.singletonList(knownWord));

        final List<String> strings = sut.extractUnknownWordsFromSentence(learner, "Hello world!");

        assertEquals(1, strings.size());
        assertEquals("world", strings.get(0));
    }

    @Test
    public void extractUnknownWordsFromSentence_noNewWord() {
        final Learner learner = new Learner();
        final KnownWord knownWord1 = new KnownWord();
        knownWord1.setWord(new Word("hello"));
        final KnownWord knownWord2 = new KnownWord();
        knownWord2.setWord(new Word("world"));
        when(repo.findAllByLearner(learner)).thenReturn(Arrays.asList(knownWord1,  knownWord2));

        final List<String> strings = sut.extractUnknownWordsFromSentence(learner, "Hello world!");

        assertTrue(strings.isEmpty());
    }

    @Test
    public void extractUnknownWordsFromSentence_allNewWords() {
        final Learner learner = new Learner();
        when(repo.findAllByLearner(learner)).thenReturn(Collections.emptyList());

        final List<String> strings = sut.extractUnknownWordsFromSentence(learner, "Hello world!");

        assertEquals(2, strings.size());
        assertEquals("hello", strings.get(0));
        assertEquals("world", strings.get(1));
    }

    @Test
    public void addNewWords() {
        final Learner learner = new Learner();
        final Collection<Word> words = Arrays.asList(new Word("hello"), new Word("world"));

        final ArgumentCaptor<List> listCaptor = ArgumentCaptor.forClass(List.class);
        when(repo.save(listCaptor.capture())).thenReturn(null);

        sut.addNewWords(words, learner);

        final List<KnownWord> list = listCaptor.getValue();
        assertEquals(2, list.size());
        assertEquals("hello", list.get(0).getWord().getText());
        assertEquals(learner, list.get(0).getLearner());
        assertEquals("world", list.get(1).getWord().getText());
        assertEquals(learner, list.get(1).getLearner());
    }

    @Test
    public void countKnownWords() {
        final int expected = 5;
        final Learner learner = new Learner();

        when(repo.findAllByLearner(learner)).thenReturn(createList(expected));

        final int actual = sut.countKnownWords(learner);

        assertEquals(expected, actual);
    }

    private List<KnownWord> createList(final int size) {
        final List<KnownWord> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            result.add(new KnownWord());
        }
        return result;
    }
}
