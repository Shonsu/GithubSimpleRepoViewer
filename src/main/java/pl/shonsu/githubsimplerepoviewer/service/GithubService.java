package pl.shonsu.githubsimplerepoviewer.service;

import org.springframework.stereotype.Service;
import pl.shonsu.githubsimplerepoviewer.model.GithubRepository;
import pl.shonsu.githubsimplerepoviewer.model.GithubSimpleRepository;
import pl.shonsu.githubsimplerepoviewer.model.SimpleBranch;

import java.util.List;

@Service
public class GithubService {
    private final GithubClient githubClient;

    public GithubService(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    public List<GithubSimpleRepository> createNotForkUserSimpleRepositories(String username){
        List<GithubRepository> repositories = githubClient.getRepositoriesByUsername(username);
        return  repositories.stream()
                .filter(GithubRepository::notFork)
                .map(repo-> new GithubSimpleRepository(
                        repo.name(),
                        repo.ownerLogin(),
                        getSimpleUserRepositoryBranches(repo)
                ))
                .toList();
    }

    private List<SimpleBranch> getSimpleUserRepositoryBranches(GithubRepository repo) {
        return githubClient.getUserRepositoryBranches(
                        repo.ownerLogin(),
                        repo.name()
                ).stream()
                .map(githubBranch -> new SimpleBranch(
                        githubBranch.name(),
                        githubBranch.sha()
                )).toList();
    }
}
