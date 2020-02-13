package com.alexander.arenatest.viewmodel;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.alexander.arenatest.model.PullRequest;
import com.alexander.arenatest.model.PullRequestsModel;
import com.alexander.arenatest.model.Repository;
import com.alexander.arenatest.view.list.adapter.PullRequestAdapter;
import java.util.List;

public class PullRequestsViewModel extends ViewModel {

    private PullRequestsModel prsModel;
    private PullRequestAdapter adapter;
    public MutableLiveData<PullRequest> pullRequestSelected;


    public void init() {
        prsModel = new PullRequestsModel();
        pullRequestSelected = new MutableLiveData<>();
        this.adapter = new PullRequestAdapter(this);
    }

    public void fetchList(Repository repository) {
        prsModel.fetchList(repository);
    }

    public MutableLiveData<List<PullRequest>> getPrsModel() {
        return prsModel.getPrs();
    }

    public PullRequestAdapter getAdapter() {
        return adapter;
    }

    public MutableLiveData<PullRequest> getSelected() {
        return pullRequestSelected;
    }

    public void setPrsInAdapter(List<PullRequest> prs) {
        this.adapter.setPrs(prs);
        this.adapter.notifyDataSetChanged();
    }

    public void onItemClick(PullRequest pullRequest) {
        pullRequestSelected.setValue(pullRequest);
    }

}