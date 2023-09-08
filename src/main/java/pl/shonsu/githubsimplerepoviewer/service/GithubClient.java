package pl.shonsu.githubsimplerepoviewer.service;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pl.shonsu.githubsimplerepoviewer.exception.GithubNotFoundException;
import pl.shonsu.githubsimplerepoviewer.model.GithubBranch;
import pl.shonsu.githubsimplerepoviewer.model.GithubRepository;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class GithubClient {

    private final Validator validator;
    private final WebClient webClient;

    public GithubClient(Validator validator, WebClient webClient) {
        this.validator = validator;
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
                .flatMap(this::validate)
                .block();
    }

    public List<GithubBranch> getUserRepositoryBranches(String owner, String repository) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/repos/{owner}/{repository}/branches")
                        .build(owner, repository))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<GithubBranch>>() {
                })
                .flatMap(this::validate)
                .block();
    }

    private <T> Mono<List<T>> validate(List<T> items) {
        Set<ConstraintViolation<T>> violations = new HashSet<>();
        items.forEach(item -> violations.addAll(
                validator.validate(item)));
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return Mono.just(items);
    }
}
