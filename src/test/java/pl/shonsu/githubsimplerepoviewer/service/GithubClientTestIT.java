package pl.shonsu.githubsimplerepoviewer.service;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class GithubClientTestIT {

    @Autowired
    private GithubClient githubClient;

    @RegisterExtension
    public static WireMockExtension server = WireMockExtension.newInstance().options(options().dynamicPort()).build();

    @DynamicPropertySource
    private static void configurationProperties(DynamicPropertyRegistry registry) {
        registry.add("github.api.base-url", server::baseUrl);
    }

    @Test
    void givenNullValueInParameterResponse_WhenGetUserRepos_ThenThrowException() {
        server.stubFor(get("/users/Shonsu/repos")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("invalidResponseForRepos.json")
                ));

        assertThrows(
                ConstraintViolationException.class,
                () -> githubClient.getRepositoriesByUsername("Shonsu"),
                "name: nie może mieć wartości null"
        );
    }

    @Test
    void givenNullValueInParameterResponse_WhenGetBranchesForRepo_ThenThrowException() {
        server.stubFor(get("/repos/Shonsu/repo/branches")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("invalidResponseForBranches.json")
                ));

        assertThrows(
                ConstraintViolationException.class,
                () -> githubClient.getUserRepositoryBranches("Shonsu", "repo"),
                "name: nie może mieć wartości null"
        );
    }
}