package com.alexander.arenatest.api;

import com.alexander.arenatest.model.PullRequest;
import com.alexander.arenatest.model.RepositoriesReturn;
import com.alexander.arenatest.model.Repository;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import rx.Observable;

public class GitHubApi {
    private static RestTemplate restTemplate;
    private static String url = "https://api.github.com";

    static {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setReadTimeout(30000);
        requestFactory.setConnectTimeout(30000);
        restTemplate = new RestTemplate(requestFactory);
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
    }

    public static Observable<ResponseEntity<RepositoriesReturn>> getRepositories(int page) {
        return Observable.create(subscriber -> {
            try {

                ResponseEntity<RepositoriesReturn> responseEntity = restTemplate.getForEntity(url + "/search/repositories?q=language:Java&sort=stars&page=" + page, RepositoriesReturn.class);
                subscriber.onNext(responseEntity);
                subscriber.onCompleted();
            } catch (Exception ex) {
                subscriber.onError(ex);
            }
        });
    }

    public static Observable<ResponseEntity<PullRequest[]>> getPulls(Repository repository) {
        return Observable.create(subscriber -> {
            try {

                ResponseEntity<PullRequest[]> responseEntity = restTemplate.getForEntity(url + "/repos/" + repository.getOwner().getName() + "/" + repository.getName() + "/pulls", PullRequest[].class);
                subscriber.onNext(responseEntity);
                subscriber.onCompleted();
            } catch (Exception ex) {
                subscriber.onError(ex);
            }
        });
    }

}
