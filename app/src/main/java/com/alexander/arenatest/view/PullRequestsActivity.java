package com.alexander.arenatest.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alexander.arenatest.R;
import com.alexander.arenatest.model.Repository;
import com.alexander.arenatest.util.UiUtil;
import com.alexander.arenatest.viewmodel.PullRequestsViewModel;
import com.google.android.material.snackbar.Snackbar;


public class PullRequestsActivity extends AppCompatActivity {
    private PullRequestsViewModel viewModel;
    private RecyclerView rvPrs;
    private ProgressBar progressBar;
    private Snackbar errorSnack;
    private LinearLayoutManager layoutManager;
    private Parcelable listState;
    private final String LIST_STATE = "listState";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_requests);
        progressBar = findViewById(R.id.progress);
        errorSnack = UiUtil.makeErrorSnack(getResources().getString(R.string.error_message), findViewById(R.id.parent_view));

        Repository repository = ((Repository) getIntent().getSerializableExtra("repository"));

        getSupportActionBar().setTitle(repository.getName());
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bindView(savedInstanceState, repository);
    }

    private void bindView(Bundle savedInstanceState, Repository repository) {
        rvPrs = findViewById(R.id.rv_pull_requests);
        rvPrs.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        rvPrs.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvPrs.getContext(),
                layoutManager.getOrientation());
        rvPrs.addItemDecoration(dividerItemDecoration);

        viewModel = ViewModelProviders.of(this).get(PullRequestsViewModel.class);

        if (savedInstanceState == null) {
            viewModel.init();
        }

        rvPrs.setAdapter(viewModel.getAdapter());

        setupOnListItemClick();
        setupOnUpdateList();
        fetchList(repository);
    }

    private void fetchList(Repository repository) {
        progressBar.setVisibility(View.VISIBLE);
        viewModel.fetchList(repository);
    }

    private void setupOnListItemClick() {
        viewModel.getSelected().observe(this, pullRequest -> {
            Intent browserIntent =
                    new Intent(Intent.ACTION_VIEW, Uri.parse(pullRequest.getUrl()));
            startActivity(browserIntent);
        });
    }

    private void setupOnUpdateList() {
        viewModel.getPrsModel().observe(this, prs -> {
            progressBar.setVisibility(View.GONE);
            if(prs != null)
                viewModel.setPrsInAdapter(prs);
            else
                errorSnack.show();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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