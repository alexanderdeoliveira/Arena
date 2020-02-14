package com.alexander.arenatest.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.alexander.arenatest.R;
import com.alexander.arenatest.util.EnumUtil;
import com.alexander.arenatest.util.UiUtil;
import com.alexander.arenatest.viewmodel.RepositoriesViewModel;
import com.google.android.material.snackbar.Snackbar;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RepositoriesViewModel viewModel;
    private RecyclerView rvRepositories;
    private SwipeRefreshLayout swipeRefresh;
    private Snackbar initialErrorSnack;
    private Snackbar errorSnack;
    private LinearLayoutManager layoutManager;
    private Parcelable listState;
    private final String LIST_STATE = "listState";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle(R.string.activity_main_title);
        getSupportActionBar().setHomeButtonEnabled(true);
        bindView(savedInstanceState);
    }

    private void bindView(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        errorSnack = UiUtil.makeErrorSnack(getResources().getString(R.string.error_message), findViewById(R.id.parent_view));
        initialErrorSnack = UiUtil.makeErrorSnack(getResources().getString(R.string.initial_error_message), findViewById(R.id.parent_view));

        swipeRefresh = findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnRefreshListener(this);

        rvRepositories = findViewById(R.id.rv_repositories);
        rvRepositories.setHasFixedSize(false);

        layoutManager = new LinearLayoutManager(this);
        rvRepositories.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvRepositories.getContext(),
                layoutManager.getOrientation());
        rvRepositories.addItemDecoration(dividerItemDecoration);

        viewModel = ViewModelProviders.of(this).get(RepositoriesViewModel.class);

        if (savedInstanceState == null) {
            viewModel.init();
        }

        rvRepositories.setAdapter(viewModel.getAdapter());

        setupOnListItemClick();
        setupOnUpdateList();
        setupOnUpdateNetworkState();

    }

    private void setupOnListItemClick() {
        viewModel.getSelected().observe(this, repository -> {
            if (repository != null) {
                Intent intent = new Intent(this, PullRequestsActivity.class);
                intent.putExtra("repository", repository);
                startActivity(intent);
            }
        });
    }

    private void setupOnUpdateList() {
        viewModel.getRepositoryLiveData().observe(this, pagedList -> {
            if(pagedList != null)
                viewModel.getAdapter().submitList(pagedList);

            swipeRefresh.setRefreshing(false);

        });
    }

    private void setupOnUpdateNetworkState() {
        viewModel.getNetworkState().observe(this, networkState -> {
            viewModel.getAdapter().setNetworkState(networkState);
            if(networkState == EnumUtil.NetworkState.FAILED) {
                errorSnack.show();
            }
        });

        viewModel.getInitialNetworkState().observe(this, networkState -> {
            viewModel.getAdapter().setNetworkState(networkState);
            if(networkState == EnumUtil.NetworkState.FAILED)
                initialErrorSnack.show();
        });
    }

    @Override
    public void onRefresh() {
        viewModel.refresh();
    }

    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

        listState = layoutManager.onSaveInstanceState();
        state.putParcelable(LIST_STATE, listState);
    }

    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);

        if(state != null)
            listState = state.getParcelable(LIST_STATE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (listState != null) {
            layoutManager.onRestoreInstanceState(listState);
        }
    }
}