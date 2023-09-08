package pl.shonsu.githubsimplerepoviewer.model;

import jakarta.validation.constraints.NotNull;

public record GithubRepository(@NotNull String name, @NotNull SimpleGithubUser owner, @NotNull boolean fork) {
    public boolean notFork(){
        return !fork;
    }
    public String ownerLogin(){
        return owner.login();
    }
}
