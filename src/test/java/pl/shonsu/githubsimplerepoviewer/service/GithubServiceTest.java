package pl.shonsu.githubsimplerepoviewer.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.shonsu.githubsimplerepoviewer.model.GithubRepository;
import pl.shonsu.githubsimplerepoviewer.model.GithubSimpleRepository;
import pl.shonsu.githubsimplerepoviewer.model.SimpleGithubUser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GithubServiceTest {
    private static final boolean FORK = true;
    private static final boolean NOT_FORK = false;
    @Mock
    private GithubClient githubClient;

    @InjectMocks
    private GithubService githubService;

    @Test
    void shouldCreateNotForkUserSimpleRepositories(){
        //given
        when(githubClient.getRepositoriesByUsername(any(String.class))).thenReturn(List.of(
                new GithubRepository("Repository name", new SimpleGithubUser("UserLogin"), FORK),
                new GithubRepository("Repository name", new SimpleGithubUser("UserLogin"), NOT_FORK),
                new GithubRepository("Repository name", new SimpleGithubUser("UserLogin"), FORK)
        ));
        //when
        List<GithubSimpleRepository> repositories = githubService.createNotForkUserSimpleRepositories("UserLogin");
        //then
        assertEquals(1, repositories.size());
    }

    @Test
    void shouldCreateEmptyUserSimpleRepositories(){
        //given
        when(githubClient.getRepositoriesByUsername(any(String.class))).thenReturn(List.of(
                new GithubRepository("Repository name", new SimpleGithubUser("UserLogin"), FORK),
                new GithubRepository("Repository name", new SimpleGithubUser("UserLogin"), FORK),
                new GithubRepository("Repository name", new SimpleGithubUser("UserLogin"), FORK)
        ));
        //when
        List<GithubSimpleRepository> repositories = githubService.createNotForkUserSimpleRepositories("UserLogin");
        //then
        assertEquals(0, repositories.size());
    }

}