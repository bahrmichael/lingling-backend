package de.lingling.backend;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection.H2;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.lingling.backend.web.Headers;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = BackendApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = H2)
@TestPropertySource(locations = "classpath:config/application-dev.properties")
public class BackendApplicationIntegrationTests {

    public static final String BASE_URL = "";
//    public static final String BASE_URL = "http://52.213.36.32:8080";


    @Autowired
    private MockMvc mvc;

    @Test
    public void homeRun() throws Exception {
        final String alexaId = "integrationtest-" + System.currentTimeMillis();

        mvc.perform(post("/init").header(Headers.ALEXA_ID, alexaId).header(Headers.ALEXA_LANGUAGE, "en"))
                .andExpect(status().isCreated());

        mvc.perform(get("/languages").header(Headers.ALEXA_ID, alexaId).header(Headers.ALEXA_LANGUAGE, "en"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]", is("German")));

        mvc.perform(put("/learner/German").header(Headers.ALEXA_ID, alexaId).header(Headers.ALEXA_LANGUAGE, "en"))
                .andExpect(status().isOk());

        mvc.perform(get("/onboarding-completed").header(Headers.ALEXA_ID, alexaId).header(Headers.ALEXA_LANGUAGE, "en"))
                .andExpect(status().isNoContent());

        // FREQUENCY WORDS

        mvc.perform(get("/frequency-words").header(Headers.ALEXA_ID, alexaId).header(Headers.ALEXA_LANGUAGE, "en"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(jsonPath("$", is("Haus")));

        mvc.perform(get("/frequency-words").header(Headers.ALEXA_ID, alexaId).header(Headers.ALEXA_LANGUAGE, "en"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(jsonPath("$", is("Haus")));

        mvc.perform(post("/frequency-words/ok").header(Headers.ALEXA_ID, alexaId).header(Headers.ALEXA_LANGUAGE, "en"))
                .andExpect(status().isOk());

        mvc.perform(get("/frequency-words").header(Headers.ALEXA_ID, alexaId).header(Headers.ALEXA_LANGUAGE, "en"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(jsonPath("$", is("Butter")));

    }

}
