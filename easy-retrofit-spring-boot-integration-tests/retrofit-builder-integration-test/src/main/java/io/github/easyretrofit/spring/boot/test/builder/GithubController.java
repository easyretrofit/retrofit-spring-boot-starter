package io.github.easyretrofit.spring.boot.test.builder;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class GithubController {

    @Resource
    private GitHubApi gitHubApi;

    public List<Contributor> getContributors() throws IOException {
        return gitHubApi.contributors("square", "retrofit").execute().body();
    }

    public List<Contributor> getContributorsJava8() throws ExecutionException, InterruptedException {
        return gitHubApi.contributorsAsync("square", "retrofit").get();
    }

    public List<Contributor> getContributorsGuava() throws ExecutionException, InterruptedException {
        return gitHubApi.listenableFuture("square", "retrofit").get();
    }
}
