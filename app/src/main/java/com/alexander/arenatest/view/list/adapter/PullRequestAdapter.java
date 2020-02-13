package com.alexander.arenatest.view.list.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import com.alexander.arenatest.BR;
import com.alexander.arenatest.R;
import com.alexander.arenatest.model.PullRequest;
import com.alexander.arenatest.viewmodel.PullRequestsViewModel;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

public class PullRequestAdapter extends RecyclerView.Adapter<PullRequestAdapter.PullRequestViewHolder> {

    private List<PullRequest> prs = new ArrayList<>();
    private PullRequestsViewModel viewModel;

    public PullRequestAdapter(PullRequestsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void setPrs(List<PullRequest> prs) {
        this.prs = prs;
    }


    public void addToList(List<PullRequest> pulls) {
        pulls.addAll(pulls);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public PullRequestAdapter.PullRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.listitem_pull_request, parent, false);

        return new PullRequestAdapter.PullRequestViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PullRequestAdapter.PullRequestViewHolder holder, int position) {
        holder.setPullRequest(prs.get(position));

    }

    @Override
    public int getItemCount() {
        return prs.size();
    }

    class PullRequestViewHolder extends RecyclerView.ViewHolder {
        private final ViewDataBinding binding;
        private ImageView ivPhoto;

        public PullRequestViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            ivPhoto = itemView.findViewById(R.id.iv_owner_photo);

        }

        public void setPullRequest(PullRequest pullRequest) {
            Glide.with(ivPhoto).load(pullRequest.getOwner().getPhotoUrl()).into(ivPhoto);
            binding.setVariable(BR.pullRequest, pullRequest);
            binding.setVariable(BR.pullRequestViewModel, viewModel);


        }
    }
}
