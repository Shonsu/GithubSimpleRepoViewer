package pl.shonsu.githubsimplerepoviewer.config.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.shonsu.githubsimplerepoviewer.config.exception.XmlNotAcceptableRepresentationException;
import pl.shonsu.githubsimplerepoviewer.config.model.GithubSimpleRepository;
import pl.shonsu.githubsimplerepoviewer.config.service.GithubService;

import java.util.List;

@RestController
public class GithubViewerController {
    private final GithubService githubService;

    public GithubViewerController(GithubService githubService) {
        this.githubService = githubService;
    }

    @GetMapping(value = "/simplerepository/{username}")
    List<GithubSimpleRepository> getUserSimpleRepositories(@RequestHeader(HttpHeaders.ACCEPT) String header, @PathVariable String username){
        if(MediaType.APPLICATION_XML_VALUE.equals(header)){
            throw new XmlNotAcceptableRepresentationException("Media type XML is not acceptable representation.");
        }
        return githubService.createNotForkUserSimpleRepositories(username);
    }
}
