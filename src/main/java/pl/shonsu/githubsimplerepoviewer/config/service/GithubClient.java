package pl.shonsu.githubsimplerepoviewer.config.service;


import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pl.shonsu.githubsimplerepoviewer.config.exception.GithubNotFoundException;
import pl.shonsu.githubsimplerepoviewer.config.model.GithubBranch;
import pl.shonsu.githubsimplerepoviewer.config.model.GithubRepository;

import java.util.List;

@Component
public class GithubClient {
    private final WebClient webClient;


    public GithubClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<GithubRepository> getRepositoriesByUsername(String username) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/users/{login}/repos")
                        .build(username))
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, response ->
                        response.bodyToMono(String.class)
                                .map(s -> new GithubNotFoundException("User not found.")))
                .bodyToMono(new ParameterizedTypeReference<List<GithubRepository>>() {
                })
                .block();
    }

    public List<GithubBranch> getUserRepositoryBranches(String owner, String repository){
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/repos/{owner}/{repository}/branches")
                        .build(owner, repository))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<GithubBranch>>() {
                })
                .block();
    }
}
