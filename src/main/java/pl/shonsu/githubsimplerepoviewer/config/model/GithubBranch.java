package pl.shonsu.githubsimplerepoviewer.config.model;

public record GithubBranch(String name, Commit commit) {

    public String sha(){
        return commit.sha();
    }
    private record Commit(String sha){}
}
