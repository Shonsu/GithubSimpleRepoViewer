package pl.shonsu.githubsimplerepoviewer.model;

import java.util.List;

public record GithubSimpleRepository(String repositoryName, String owner, List<SimpleBranch> branches) {
}
