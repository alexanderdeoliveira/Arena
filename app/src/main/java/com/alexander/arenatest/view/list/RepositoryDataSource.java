package com.alexander.arenatest.view.list;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;
import com.alexander.arenatest.model.Repository;
import com.alexander.arenatest.api.GitHubApi;
import com.alexander.arenatest.util.EnumUtil;
import java.util.List;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RepositoryDataSource extends PageKeyedDataSource<Long, Repository> {

    private MutableLiveData networkState;
    private MutableLiveData initialLoading;
    MutableLiveData<List<Repository>> repositories = new MutableLiveData<>();


    public RepositoryDataSource() {
        networkState = new MutableLiveData();
        initialLoading = new MutableLiveData();
    }


    public MutableLiveData getNetworkState() {
        return networkState;
    }

    public MutableLiveData getInitialLoading() {
        return initialLoading;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params,
                            @NonNull LoadInitialCallback<Long, Repository> callback) {

        initialLoading.postValue(EnumUtil.NetworkState.LOADING);

        GitHubApi.getRepositories(1)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            callback.onResult(response.getBody().getRepositories(), null, 2l);
                            initialLoading.postValue(EnumUtil.NetworkState.LOADED);
                        },
                        error -> {
                            repositories.setValue(null);
                            initialLoading.postValue(EnumUtil.NetworkState.FAILED);
                        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Repository> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params,
                          @NonNull LoadCallback<Long, Repository> callback) {


        networkState.postValue(EnumUtil.NetworkState.LOADING);

        GitHubApi.getRepositories(params.key.intValue())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            callback.onResult(response.getBody().getRepositories(), params.key+1);
                            networkState.postValue(EnumUtil.NetworkState.LOADED);
                        },
                        error -> {
                            repositories.setValue(null);
                            networkState.postValue(EnumUtil.NetworkState.FAILED);
                        });
    }
}
