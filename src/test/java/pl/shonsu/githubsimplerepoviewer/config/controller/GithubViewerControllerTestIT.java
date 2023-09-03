package pl.shonsu.githubsimplerepoviewer.config.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.shonsu.githubsimplerepoviewer.config.exception.ErrorMessage;
import pl.shonsu.githubsimplerepoviewer.config.exception.GithubNotFoundException;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GithubViewerControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance().build();

    @DynamicPropertySource
    static void configurationProperties(DynamicPropertyRegistry registry) {
        registry.add("github.api.base-url", wireMockServer::baseUrl);
    }

    @Test
    void shouldThrowExceptionWhenStatusNotFound() throws Exception {
        wireMockServer.stubFor(WireMock.get("/users/Shonsu/repos")
                .willReturn(aResponse()
                        .withBody("bla blabla blablabla")
                        .withStatus(HttpStatus.NOT_FOUND.value())));

        MvcResult requestResult = mockMvc.perform(MockMvcRequestBuilders.get("/simplerepository/Shonsu").accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof GithubNotFoundException)).andReturn();
        ErrorMessage errorMessage = objectMapper.readValue(requestResult.getResponse().getContentAsByteArray(), new TypeReference<ErrorMessage>() {
        });

        assertEquals("404 NOT_FOUND", errorMessage.status());
        assertEquals("User not found.", errorMessage.message());
    }
}
