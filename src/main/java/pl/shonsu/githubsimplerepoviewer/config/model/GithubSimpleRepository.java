package pl.shonsu.githubsimplerepoviewer.config.model;

import java.util.List;

public record GithubSimpleRepository(String repositoryName, String owner, List<SimpleBranch> branches) {
}
