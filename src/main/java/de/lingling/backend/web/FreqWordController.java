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

import de.lingling.backend.domain.Account;
import de.lingling.backend.domain.Action;
import de.lingling.backend.domain.Learner;
import de.lingling.backend.domain.Word;
import de.lingling.backend.service.AccountService;
import de.lingling.backend.service.AuditService;
import de.lingling.backend.service.KnownWordService;
import de.lingling.backend.service.LearnerService;
import de.lingling.backend.service.WordService;

@Controller
@EnableTransactionManagement
@RequestMapping("/frequency-word")
public class FreqWordController {

    private final AccountService accountService;
    private final LearnerService learnerService;
    private final WordService wordService;
    private final AuditService auditService;
    private final KnownWordService knownWordService;

    public FreqWordController(final AccountService accountService,
            final LearnerService learnerService, final WordService wordService,
            final AuditService auditService, final KnownWordService knownWordService) {
        this.accountService = accountService;
        this.learnerService = learnerService;
        this.wordService = wordService;
        this.auditService = auditService;
        this.knownWordService = knownWordService;
    }

    @GetMapping
    @ResponseBody
    @Transactional
    public String getFrequencyWord(@RequestHeader(name = Headers.ALEXA_ID, required = true) final String alexaId,
            @RequestHeader(name = Headers.UTTERANCE, required = false) final String utterance) {
        final Account account = accountService.findAccount(alexaId);
        final Learner learner = learnerService.findLatestLearnerForAccount(account);
        final String returnedWord = wordService.getNextFrequencyWord(learner).getText();
        auditService.addAudit(alexaId, utterance, Action.FREQUENCY_WORD, returnedWord);
        return returnedWord;
    }

    @PostMapping("/ok")
    @Transactional
    public void postFrequencyWOrdOk(@RequestHeader(name = Headers.ALEXA_ID, required = true) final String alexaId,
            @RequestHeader(name = Headers.UTTERANCE, required = false) final String utterance) {
        final String latestSentence = auditService.findLatestValue(alexaId);
        final Account account = accountService.findAccount(alexaId);
        final Learner learner = learnerService.findLatestLearnerForAccount(account);

        final String recentText = auditService.findLatestFrequencyWordForUser(alexaId);
        final Word newWord = wordService.findWord(recentText, learner.getLanguageDst());
        knownWordService.addNewWords(Collections.singletonList(newWord), learner);
        auditService.addAudit(alexaId, utterance, Action.FREQUENCY_WORD_OK, latestSentence);
    }

    @PostMapping("/notok")
    @Transactional
    public void postFrequencyWordNotOk(@RequestHeader(name = Headers.ALEXA_ID, required = true) final String alexaId,
            @RequestHeader(name = Headers.UTTERANCE, required = false) final String utterance) {
        final String lastFrequencyWord = auditService.findLatestFrequencyWordForUser(alexaId);
        auditService.addAudit(alexaId, utterance, Action.FREQUENCY_WORD_NOTOK, lastFrequencyWord);
    }
}
