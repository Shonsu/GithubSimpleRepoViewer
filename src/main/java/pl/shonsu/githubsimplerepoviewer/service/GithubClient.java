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
import pl.shonsu.githubsimplerepoviewer.model.GithubSimpleRepository;
import pl.shonsu.githubsimplerepoviewer.model.SimpleBranch;
import reactor.core.publisher.Flux;
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

    public Mono<List<GithubBranch>> getUserRepositoryBranchesMono(String owner, String repository) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/repos/{owner}/{repository}/branches")
                        .build(owner, repository))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<GithubBranch>>() {
                })
                .flatMap(this::validate);
    }

    public List<GithubBranch> getUserRepositoryBranchesInParallel(String owner, List<GithubRepository> repositories) {
        return Flux.fromIterable(repositories)
                .flatMap(repo -> getUserRepositoryBranchesMono(owner, repo.name()).zipWith(repo, branches ->
                                new GithubSimpleRepository(repo.name(), repo.ownerLogin(), branches.stream().map(githubBranch -> new SimpleBranch(
                                                githubBranch.name(),
                                                githubBranch.sha()
                                        )).toList()
                                ))
                )
//
//                .toStream()
//                .flatMap(List::stream)
//                .toList();
    }


//    private GithubSimpleRepository createSimpleRepo(String repoName,String repoOwner, Mono<List<GithubBranch>> monoBranches){
//        return monoBranches.share().block(githubBranches -> new GithubSimpleRepository(repoName, repoOwner, githubBranches.stream().map(githubBranch -> new SimpleBranch(githubBranch.name(), githubBranch.sha())).toList()));
//    }


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
