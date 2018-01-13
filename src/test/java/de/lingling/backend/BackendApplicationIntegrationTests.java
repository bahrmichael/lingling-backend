package de.lingling.backend;

import java.io.IOException;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONArray;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import de.lingling.backend.domain.Language;
import de.lingling.backend.domain.LanguageName;
import de.lingling.backend.domain.Word;
import de.lingling.backend.web.Headers;

@Ignore
public class BackendApplicationIntegrationTests {

    public static final String BASE_URL = "http://localhost:8080";
//    public static final String BASE_URL = "http://52.213.36.32:8080";

    @Test
    public void prep() throws Exception {
        // PREP
        final Language language = new Language();
        language.setLanguageCode("en");
        HttpResponse<JsonNode> postLanguage1 = Unirest.post(BASE_URL + "/languages")
                                                      .header("Content-Type","application/json")
                                                      .body(TestUtil.convertObjectToJsonBytes(language))
                                                      .asJson();
        assertEquals(200, postLanguage1.getStatus());

        final Language language2 = new Language();
        language2.setLanguageCode("de");
        HttpResponse<JsonNode> postLanguage2 = Unirest.post(BASE_URL + "/languages")
                                                      .header("Content-Type","application/json")
                                                      .body(TestUtil.convertObjectToJsonBytes(language2))
                                                      .asJson();
        assertEquals(200, postLanguage2.getStatus());

        final LanguageName languageName = new LanguageName();
        languageName.setLanguageSrc(language);
        languageName.setLanguageDst(language2);
        languageName.setName("German");
        HttpResponse<JsonNode> postLanguageName = Unirest.post(BASE_URL + "/language-names")
                                                         .header("Content-Type", "application/json")
                                                         .body(TestUtil.convertObjectToJsonBytes(languageName))
                                                         .asJson();
        assertEquals(200, postLanguageName.getStatus());
    }

    @Test
    public void addFrequencyWords() throws Exception {
        addWord(10L, "Baum");
        addWord(20L, "Kaffee");
        addWord(30L, "Vogel");
        addWord(40L, "Butter");
        addWord(50L, "Haus");
    }

    private void addWord(final long frequency, final String text) throws UnirestException, IOException {
        final Word word = new Word();
        word.setFrequency(frequency);
        word.setText(text);
        HttpResponse<JsonNode> response = Unirest.post(BASE_URL + "/frequency-words/de")
                .header("Content-Type", "application/json")
                .body(TestUtil.convertObjectToJsonBytes(word))
                .asJson();
        assertEquals(200, response.getStatus());
    }

    @Test
    public void test() throws Exception {
        // RUN
        String alexaId = "alexa-" + System.currentTimeMillis();

        HttpResponse<String> signup = Unirest.post(BASE_URL + "/signup").header(Headers.ALEXA_ID, alexaId).header(Headers.ALEXA_LANGUAGE, "en-US").asString();
        assertEquals(200, signup.getStatus());

        HttpResponse<String> response = Unirest.get(BASE_URL + "/latest-interaction").header(Headers.ALEXA_ID, alexaId).asString();
        assertEquals(404, response.getStatus());

        HttpResponse<String> response2 = Unirest.get(BASE_URL + "/latest-interaction").header(Headers.ALEXA_ID, alexaId).asString();
        assertEquals(200, response2.getStatus());

        HttpResponse<JsonNode> response3 = Unirest.get(BASE_URL + "/languages").header(Headers.ALEXA_ID, alexaId).header(Headers.ALEXA_LANGUAGE, "en-US").asJson();
        assertEquals(200, response3.getStatus());
        JSONArray languagesArray = response3.getBody().getArray();
        assertEquals(1, languagesArray.length());
        String possibleDstLanguage = languagesArray.getString(0);
        assertEquals("German", possibleDstLanguage);

        HttpResponse<JsonNode> response4 = Unirest.put(BASE_URL + "/learner/" + possibleDstLanguage).header(Headers.ALEXA_ID, alexaId).asJson();
        assertEquals(200, response4.getStatus());


    }

}
