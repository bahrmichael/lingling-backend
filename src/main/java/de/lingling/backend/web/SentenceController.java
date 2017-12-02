package de.lingling.backend.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import de.lingling.backend.domain.Account;
import de.lingling.backend.domain.Action;
import de.lingling.backend.domain.Language;
import de.lingling.backend.domain.Learner;
import de.lingling.backend.domain.Word;
import de.lingling.backend.service.AccountService;
import de.lingling.backend.service.AuditService;
import de.lingling.backend.service.KnownWordService;
import de.lingling.backend.service.LearnerService;
import de.lingling.backend.service.SentenceService;
import de.lingling.backend.service.WordService;

@Controller
@EnableTransactionManagement
@RequestMapping("/sentence")
public class SentenceController {

    private final AccountService accountService;
    private final LearnerService learnerService;
    private final SentenceService sentenceService;
    private final AuditService auditService;
    private final WordService wordService;
    private final KnownWordService knownWordService;

    public SentenceController(final AccountService accountService,
            final LearnerService learnerService, final SentenceService sentenceService,
            final AuditService auditService, final WordService wordService,
            final KnownWordService knownWordService) {
        this.accountService = accountService;
        this.learnerService = learnerService;
        this.sentenceService = sentenceService;
        this.auditService = auditService;
        this.wordService = wordService;
        this.knownWordService = knownWordService;
    }

    @GetMapping
    @ResponseBody
    @Transactional
    public String getSentence(@RequestHeader(name = Headers.ALEXA_ID, required = true) final String alexaId,
            @RequestHeader(name = Headers.UTTERANCE, required = false) final String utterance) {
        final Account account = accountService.findAccount(alexaId);
        final Learner learner = learnerService.findLatestLearnerForAccount(account);
        final Language src = account.getLanguageSrc();
        final Language dst = learner.getLanguageDst();
        final String returnedSentence = sentenceService.getRandomSentence(src, dst, learner).getTextDst();
        auditService.addAudit(alexaId, utterance, Action.SENTENCE, returnedSentence);
        return returnedSentence;
    }

    @PostMapping("/dev/add/{sentence}")
    @Transactional
    public void addSentence(@PathVariable("sentence") final String sentence) {
        sentenceService.devAddSentence(sentence);
    }

    @GetMapping("/repeat")
    @ResponseBody
    @Transactional
    public String repeatSentence(@RequestHeader(name = Headers.ALEXA_ID, required = true) final String alexaId,
            @RequestHeader(name = Headers.UTTERANCE, required = false) final String utterance) {
        final Account account = accountService.findAccount(alexaId);
        final String returnedSentence = sentenceService.getRecentSentence(account).getTextDst();
        auditService.addAudit(alexaId, utterance, Action.REPEAT, returnedSentence);
        return returnedSentence;
    }

    @GetMapping("/translate")
    @ResponseBody
    @Transactional
    public String getTranslation(@RequestHeader(name = Headers.ALEXA_ID, required = true) final String alexaId,
            @RequestHeader(name = Headers.UTTERANCE, required = false) final String utterance) {
        final Account account = accountService.findAccount(alexaId);
        final String returnedSentence = sentenceService.getRecentSentence(account).getTextSrc();
        auditService.addAudit(alexaId, utterance, Action.TRANSLATE, returnedSentence);
        return returnedSentence;
    }

    @PostMapping("/ok")
    @Transactional
    public void postOk(@RequestHeader(name = Headers.ALEXA_ID, required = true) final String alexaId,
            @RequestHeader(name = Headers.UTTERANCE, required = false) final String utterance) {
        final String latestSentence = auditService.findLatestSentence(alexaId);
        final Account account = accountService.findAccount(alexaId);
        final Learner learner = learnerService.findLatestLearnerForAccount(account);
        final List<String> unknownWords = knownWordService.extractUnknownWordsFromSentence(learner, latestSentence);
        final List<Word> newWords = wordService.findWords(unknownWords, learner.getLanguageDst());
        knownWordService.addNewWords(newWords, learner);
        auditService.addAudit(alexaId, utterance, Action.OK, latestSentence);
    }

    @PostMapping("/notok")
    @Transactional
    public void postNotOk(@RequestHeader(name = Headers.ALEXA_ID, required = true) final String alexaId,
            @RequestHeader(name = Headers.UTTERANCE, required = false) final String utterance) {
        final String latestSentence = auditService.findLatestSentence(alexaId);
        auditService.addAudit(alexaId, utterance, Action.NOTOK, latestSentence);
    }
}
