package io.github.easyretrofit.spring.boot.test.builder;

import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GithubControllerTest {

    @Autowired
    private GithubController githubController;

    @SneakyThrows
    @Test
    public void getContributors() {
        List<Contributor> contributors = githubController.getContributors();
        assertNotNull(contributors);
        assertFalse(contributors.isEmpty());
    }


    @SneakyThrows
    @Test
    public void getContributorsJava8() {
        List<Contributor> contributors = githubController.getContributorsJava8();
        assertNotNull(contributors);
        assertFalse(contributors.isEmpty());
    }

    @SneakyThrows
    @Test
    public void getContributorsGuava() {
        List<Contributor> contributors = githubController.getContributorsGuava();
        assertNotNull(contributors);
        assertFalse(contributors.isEmpty());
    }

}