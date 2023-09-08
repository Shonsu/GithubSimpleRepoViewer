package pl.shonsu.githubsimplerepoviewer.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.shonsu.githubsimplerepoviewer.exception.ErrorMessage;
import pl.shonsu.githubsimplerepoviewer.exception.XmlNotAcceptableRepresentationException;
import pl.shonsu.githubsimplerepoviewer.service.GithubService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GithubViewerController.class)
class GithubViewerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GithubService githubService;

    @Test
    void givenXMLAcceptHeader_ThrowException() throws Exception {
        MvcResult requestResult = mockMvc.perform(get("/simplerepository/{username}", "Shonsu")
                        .accept(MediaType.APPLICATION_XML))
                .andExpect(status().isNotAcceptable())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof XmlNotAcceptableRepresentationException))
                .andReturn();
        ErrorMessage errorMessage = objectMapper.readValue(requestResult.getResponse().getContentAsString(), new TypeReference<ErrorMessage>() {
        });

        assertEquals("406 NOT_ACCEPTABLE", errorMessage.status());
        assertEquals("Media type XML is not acceptable representation.", errorMessage.message());
    }

}