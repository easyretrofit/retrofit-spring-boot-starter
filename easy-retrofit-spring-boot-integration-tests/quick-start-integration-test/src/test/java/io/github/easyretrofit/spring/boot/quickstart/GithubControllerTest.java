package io.github.easyretrofit.spring.boot.quickstart;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.*;


@SpringBootTest
@RunWith(SpringRunner.class)
public class GithubControllerTest {

    @Autowired
    private GithubController githubController;

    @Test
    public void getContributors() throws IOException {

        String contributors = githubController.getContributors();
        assertNotNull(contributors);
    }
}