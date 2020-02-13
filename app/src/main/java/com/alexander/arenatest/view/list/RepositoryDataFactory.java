package com.alexander.arenatest.view.list;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class RepositoryDataFactory extends DataSource.Factory {

    private MutableLiveData<RepositoryDataSource> mutableLiveData;
    private RepositoryDataSource repositoryDataSource;

    public RepositoryDataFactory() {
        this.mutableLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource create() {
        repositoryDataSource = new RepositoryDataSource();
        mutableLiveData.postValue(repositoryDataSource);
        return repositoryDataSource;
    }

    public RepositoryDataSource getRepositoryDataSource() {
        return repositoryDataSource;
    }

    public MutableLiveData<RepositoryDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}
