package io.github.easyretrofit.spring.boot.quickstart;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
public class GithubController {

    @Resource
    private GitHubApi gitHubApi;

    public String getContributors() throws IOException {
        return gitHubApi.contributors("square", "retrofit").execute().body().string();
    }
}
