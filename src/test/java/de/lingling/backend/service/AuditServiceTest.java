package de.lingling.backend.service;

import java.util.Optional;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.lingling.backend.domain.Action;
import de.lingling.backend.domain.Audit;
import de.lingling.backend.repository.AuditRepository;

public class AuditServiceTest {

    private static final String ALEXA_ID = "alexaId";
    private static final String UTTERANCE = "anUtterance";
    private static final Action ACTION = Action.SENTENCE;
    private static final String RET_VAL = "aReturnedValue";
    private final AuditRepository repo = mock(AuditRepository.class);
    private final AuditService sut = new AuditService(repo);

    @Test
    public void addAudit() {
        final ArgumentCaptor<Audit> captor = ArgumentCaptor.forClass(Audit.class);
        when(repo.save(captor.capture())).thenReturn(null);

        sut.addAudit(ALEXA_ID, UTTERANCE, ACTION, RET_VAL);

        verify(repo).save(any(Audit.class));
        assertEquals(RET_VAL, captor.getValue().getReturnedValue());
        assertEquals(ALEXA_ID, captor.getValue().getAlexaId());
        assertEquals(UTTERANCE, captor.getValue().getUtterance());
        assertEquals(ACTION, captor.getValue().getAction());
    }

    @Test
    public void findLatestForUser() {
        final Audit expected = new Audit();
        when(repo.findFirstByAlexaIdOrderByTimestampDesc(ALEXA_ID)).thenReturn(expected);

        final Audit result = sut.findLatestForUser(ALEXA_ID);

        verify(repo).findFirstByAlexaIdOrderByTimestampDesc(ALEXA_ID);
        assertEquals(expected, result);
    }

    @Test
    public void findLatestValue() {
        final Audit audit = new Audit();
        audit.setReturnedValue(RET_VAL);
        final Optional<Audit> optional = Optional.of(audit);
        when(repo.findFirstByAlexaIdAndActionOrderByTimestampDesc(ALEXA_ID, Action.SENTENCE)).thenReturn(optional);

        final String latestSentence = sut.findLatestSentence(ALEXA_ID);

        assertEquals(RET_VAL, latestSentence);
        verify(repo).findFirstByAlexaIdAndActionOrderByTimestampDesc(ALEXA_ID, Action.SENTENCE);
    }

    @Test
    public void findLatestFrequencyWordForUser() {
        final Audit audit = new Audit();
        audit.setReturnedValue(RET_VAL);
        final Optional<Audit> optional = Optional.of(audit);
        when(repo.findFirstByAlexaIdAndActionOrderByTimestampDesc(ALEXA_ID, Action.FREQUENCY_WORD)).thenReturn(optional);

        final String latestSentence = sut.findLatestFrequencyWord(ALEXA_ID);

        assertEquals(RET_VAL, latestSentence);
        verify(repo).findFirstByAlexaIdAndActionOrderByTimestampDesc(ALEXA_ID, Action.FREQUENCY_WORD);
    }

    @Ignore
    @Test(expected = IllegalStateException.class)
    public void findLatestSentence_withoutResult() {
        when(repo.findFirstByAlexaIdAndActionOrderByTimestampDesc(ALEXA_ID, Action.SENTENCE)).thenReturn(Optional.empty());
        sut.findLatestSentence(ALEXA_ID);
    }
}
