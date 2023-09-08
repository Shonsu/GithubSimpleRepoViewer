package pl.shonsu.githubsimplerepoviewer.model;

import jakarta.validation.constraints.NotNull;

public record SimpleGithubUser(@NotNull String login) {
}
