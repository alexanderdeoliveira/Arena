package com.alexander.arenatest.model;


import androidx.databinding.BaseObservable;
import androidx.lifecycle.MutableLiveData;
import com.alexander.arenatest.api.GitHubApi;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PullRequestsModel extends BaseObservable {

    List<PullRequest> prsList = new ArrayList<>();
    MutableLiveData<List<PullRequest>> prs = new MutableLiveData<>();


    public MutableLiveData<List<PullRequest>> getPrs() {
        return prs;
    }

    public List<PullRequest> getPrsList() {
        return prsList;
    }

    public void fetchList(Repository repository) {
        GitHubApi.getPulls(repository)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            prsList = Arrays.asList(response.getBody());
                            prs.setValue(prsList);

                        },
                        error -> {
                            prs.setValue(null);
                        });
    }
}
