package pl.shonsu.githubsimplerepoviewer.exception;

public class GithubNotFoundException extends RuntimeException{

    public GithubNotFoundException(String message) {
        super(message);
    }
}
