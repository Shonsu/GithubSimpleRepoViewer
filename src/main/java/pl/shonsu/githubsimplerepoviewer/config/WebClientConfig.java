package pl.shonsu.githubsimplerepoviewer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${github.api.base-url}")
    private String GITHUB_BASE_URL;

    @Bean
    WebClient configure(){
        return WebClient.builder()
                .baseUrl(GITHUB_BASE_URL)
                .defaultHeaders(httpHeaders ->
                        {
                            httpHeaders.add("X-GitHub-Api-Version", "2022-11-28");
                            httpHeaders.add(HttpHeaders.ACCEPT, "application/vnd.github+json");
                        }
                )
                .build();
    }

}
