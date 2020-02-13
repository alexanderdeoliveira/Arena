package com.alexander.arenatest.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import com.alexander.arenatest.util.EnumUtil;
import com.alexander.arenatest.view.list.RepositoryDataFactory;
import com.alexander.arenatest.model.Repository;
import com.alexander.arenatest.view.list.adapter.RepositoryPageAdapter;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RepositoriesViewModel extends ViewModel {

    private RepositoryPageAdapter adapter;
    public MutableLiveData<Repository> repositorySelected;
    private Executor executor;
    private LiveData<EnumUtil.NetworkState> networkState;
    private LiveData<EnumUtil.NetworkState> initialNetworState;

    private LiveData<PagedList<Repository>> repositoryLiveData;
    private RepositoryDataFactory repositoryDataFactory;

    public void init() {
        executor = Executors.newFixedThreadPool(1);

        repositorySelected = new MutableLiveData<>();
        adapter = new RepositoryPageAdapter(this);

        repositoryDataFactory = new RepositoryDataFactory();
        networkState = Transformations.switchMap(repositoryDataFactory.getMutableLiveData(),
                dataSource -> dataSource.getNetworkState());

        initialNetworState = Transformations.switchMap(repositoryDataFactory.getMutableLiveData(),
                dataSource -> dataSource.getInitialLoading());

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(20).build();

        repositoryLiveData = (new LivePagedListBuilder(repositoryDataFactory, pagedListConfig))
                .setFetchExecutor(executor)
                .build();

    }

    public void refresh() {
        repositoryDataFactory.getRepositoryDataSource().invalidate();
    }


    public LiveData<EnumUtil.NetworkState> getNetworkState() {
        return networkState;
    }

    public LiveData<EnumUtil.NetworkState> getInitialNetworkState() {
        return initialNetworState;
    }


    public LiveData<PagedList<Repository>> getRepositoryLiveData() {
        return repositoryLiveData;
    }

    public RepositoryPageAdapter getAdapter() {
        return adapter;
    }

    public MutableLiveData<Repository> getSelected() {
        return repositorySelected;
    }

    public void onItemClick(Repository repository) {
        repositorySelected.setValue(repository);
    }
}