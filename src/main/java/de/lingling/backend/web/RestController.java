package de.lingling.backend.web;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import de.lingling.backend.domain.Account;
import de.lingling.backend.domain.Action;
import de.lingling.backend.domain.Audit;
import de.lingling.backend.domain.Language;
import de.lingling.backend.domain.LanguageName;
import de.lingling.backend.service.AccountService;
import de.lingling.backend.service.AuditService;
import de.lingling.backend.service.KnownWordService;
import de.lingling.backend.service.LanguageNameService;
import de.lingling.backend.service.LanguageService;
import de.lingling.backend.service.LearnerService;

@Controller
@EnableTransactionManagement
public class RestController {

    private static final Logger LOG = LoggerFactory.getLogger(RestController.class);

    public static final int MIN_REQUIRED_WORDS_FOR_ONBOARDING = 20;
    private final AuditService auditService;
    private final KnownWordService knownWordService;
    private final AccountService accountService;
    private final LearnerService learnerService;
    private final LanguageNameService languageNameService;
    private final LanguageService languageService;

    public RestController(final AuditService auditService,
                          final KnownWordService knownWordService,
                          final AccountService accountService,
                          final LearnerService learnerService,
                          final LanguageNameService languageNameService,
                          final LanguageService languageService) {
        this.auditService = auditService;
        this.knownWordService = knownWordService;
        this.accountService = accountService;
        this.learnerService = learnerService;
        this.languageNameService = languageNameService;
        this.languageService = languageService;
    }

    @GetMapping("/alive")
    public ResponseEntity isAlive() {
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/init")
    public ResponseEntity init(@RequestHeader(name = Headers.ALEXA_ID, required = true) final String alexaId,
                               @RequestHeader(name = Headers.UTTERANCE, required = false) final String utterance,
                               @RequestHeader(name = Headers.ALEXA_LANGUAGE, required = true) final String language) {
        final Account account = accountService.findAccount(alexaId);
        if (null == account) {
            accountService.createAccount(alexaId, language);
            return ResponseEntity.status(201).build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping(value = "/latest-interaction", produces = "text/plain")
    @ResponseBody
    @Transactional
    public ResponseEntity<String> getLatestInteraction(@RequestHeader(name = Headers.ALEXA_ID, required = true) final String alexaId,
                                                       @RequestHeader(name = Headers.UTTERANCE, required = false) final String utterance) {
        final Audit latestForUser = auditService.findLatestForUser(alexaId);
        if (null != latestForUser) {
            final Instant timestamp = latestForUser.getTimestamp();
            auditService.addAudit(alexaId, utterance, Action.LATEST_INTERACTION, timestamp.toString());
            return ResponseEntity.ok(timestamp.toString());
        } else {
            auditService.addAudit(alexaId, utterance, Action.LATEST_INTERACTION, null);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/languages", produces = "application/json")
    @ResponseBody
    @Transactional
    public ResponseEntity<List<String>> getPossibleLanguages(@RequestHeader(name = Headers.ALEXA_ID, required = true) final String alexaId,
                                             @RequestHeader(name = Headers.UTTERANCE, required = false) final String utterance,
                                             @RequestHeader(name = Headers.ALEXA_LANGUAGE, required = true) final String language) {
        final List<String> result = languageNameService.findPossibleDstLanguages(language)
                                                       .stream().map(LanguageName::getName)
                                                       .collect(Collectors.toList());
        auditService.addAudit(alexaId, utterance, Action.GET_LANGUAGES, result.stream().collect(Collectors.joining(",")));
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/languages", consumes = "application/json")
    @ResponseBody
    public void addPossibleLanguage(@RequestBody final Language language) {
        // todo: dev only
        languageService.add(language);
    }

    @PostMapping(value = "/language-names", consumes = "application/json")
    @Transactional
    public ResponseEntity addPossibleLanguageName(@RequestBody final LanguageName languageName) {
        // todo: dev only
        languageNameService.add(languageName);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/learner/{languageName}")
    @ResponseBody
    @Transactional
    public void setDstLanguage(@RequestHeader(name = Headers.ALEXA_ID, required = true) final String alexaId,
                               @RequestHeader(name = Headers.UTTERANCE, required = false) final String utterance,
                               @PathVariable final String languageName) {
        LOG.info("Invoked learner/{} with headers {} and {}", languageName, utterance, alexaId);
        final Language language = languageNameService.findLanguage(languageName);
        // todo: perform init if the account does not exist yet
        final Account account = accountService.findAccount(alexaId);
        LOG.info("Loaded account: {}", account);
        learnerService.addLearner(account, language);
        auditService.addAudit(alexaId, utterance, Action.SET_LANGUAGE,null);
    }

    @GetMapping("/onboarding-completed")
    @ResponseBody
    @Transactional
    public ResponseEntity isOnboardingCompleted(@RequestHeader(name = Headers.ALEXA_ID, required = true) final String alexaId,
                                                @RequestHeader(name = Headers.UTTERANCE, required = false) final String utterance) {

        auditService.addAudit(alexaId, utterance, Action.GET_ONBOARDING_COMPLETED,null);
        if (isOnboardingDone(alexaId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    private boolean isOnboardingDone(final String alexaId) {
        return MIN_REQUIRED_WORDS_FOR_ONBOARDING <= knownWordService.countKnownWords(learnerService.findLatestLearner(alexaId));
    }
}
