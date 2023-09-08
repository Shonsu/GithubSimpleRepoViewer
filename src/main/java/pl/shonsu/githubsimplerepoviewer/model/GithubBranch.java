package pl.shonsu.githubsimplerepoviewer.model;

import jakarta.validation.constraints.NotNull;

public record GithubBranch(@NotNull String name, @NotNull Commit commit) {

    public String sha(){
        return commit.sha();
    }
    private record Commit(@NotNull String sha){}
}
