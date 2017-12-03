package de.lingling.backend.web;

import java.util.Collections;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import de.lingling.backend.domain.Action;
import de.lingling.backend.domain.Word;
import de.lingling.backend.service.AuditService;
import de.lingling.backend.service.KnownWordService;
import de.lingling.backend.service.WordService;

@Controller
@EnableTransactionManagement
@RequestMapping("/frequency-word")
public class FreqWordController {

    private final WordService wordService;
    private final AuditService auditService;
    private final KnownWordService knownWordService;

    public FreqWordController(final WordService wordService, final AuditService auditService,
            final KnownWordService knownWordService) {
        this.wordService = wordService;
        this.auditService = auditService;
        this.knownWordService = knownWordService;
    }

    @GetMapping
    @ResponseBody
    @Transactional
    public String getFrequencyWord(@RequestHeader(name = Headers.ALEXA_ID, required = true) final String alexaId,
            @RequestHeader(name = Headers.UTTERANCE, required = false) final String utterance) {
        final String returnedWord = wordService.getNextFrequencyWord(alexaId).getText();
        auditService.addAudit(alexaId, utterance, Action.FREQUENCY_WORD, returnedWord);
        return returnedWord;
    }

    @PostMapping("/ok")
    @Transactional
    public void postFrequencyWOrdOk(@RequestHeader(name = Headers.ALEXA_ID, required = true) final String alexaId,
            @RequestHeader(name = Headers.UTTERANCE, required = false) final String utterance) {
        final Word newWord = wordService.findWord(auditService.findLatestFrequencyWord(alexaId), alexaId);
        knownWordService.addNewWords(Collections.singletonList(newWord), alexaId);
        auditService.addAudit(alexaId, utterance, Action.FREQUENCY_WORD_OK, auditService.findLatestSentence(alexaId));
    }

    @PostMapping("/notok")
    @Transactional
    public void postFrequencyWordNotOk(@RequestHeader(name = Headers.ALEXA_ID, required = true) final String alexaId,
            @RequestHeader(name = Headers.UTTERANCE, required = false) final String utterance) {
        auditService.addAudit(alexaId, utterance, Action.FREQUENCY_WORD_NOTOK, auditService.findLatestFrequencyWord(alexaId));
    }
}
