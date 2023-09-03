package pl.shonsu.githubsimplerepoviewer.config.model;

public record GithubRepository(String name, SimpleGithubUser owner, boolean fork) {
    public boolean notFork(){
        return !fork;
    }
    public String ownerLogin(){
        return owner.login();
    }
}
