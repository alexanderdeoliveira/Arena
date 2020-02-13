package com.alexander.arenatest.view.list.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.paging.PagedListAdapter;
import com.alexander.arenatest.BR;
import com.alexander.arenatest.util.EnumUtil;
import com.alexander.arenatest.R;
import com.alexander.arenatest.model.Repository;
import com.alexander.arenatest.view.list.BaseViewHolder;
import com.alexander.arenatest.viewmodel.RepositoriesViewModel;
import com.bumptech.glide.Glide;
import butterknife.ButterKnife;

public class RepositoryPageAdapter extends PagedListAdapter<Repository, BaseViewHolder> {

    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private RepositoriesViewModel viewModel;
    private EnumUtil.NetworkState networkState;


    public RepositoryPageAdapter(RepositoriesViewModel viewModel) {
        super(Repository.DIFF_CALLBACK);
        this.viewModel = viewModel;
    }


    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.listitem_repository, parent, false);

                return new RepositoryViewHolder(binding);

            case VIEW_TYPE_LOADING:
                return new ProgressHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_loading, parent, false));
            default:
                return null;
        }
    }


    /*
     * Default method of RecyclerView.Adapter
     */
    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);

    }


    /*
     * Default method of RecyclerView.Adapter
     */
    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return VIEW_TYPE_LOADING;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }


    private boolean hasExtraRow() {
        if (networkState != null && networkState != EnumUtil.NetworkState.LOADED) {
            return true;
        } else {
            return false;
        }
    }

    public void setNetworkState(EnumUtil.NetworkState newNetworkState) {
        EnumUtil.NetworkState previousState = this.networkState;
        boolean previousExtraRow = hasExtraRow();
        this.networkState = newNetworkState;
        boolean newExtraRow = hasExtraRow();
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(getItemCount());
            } else {
                notifyItemInserted(getItemCount());
            }
        } else if (newExtraRow && previousState != newNetworkState) {
            notifyItemChanged(getItemCount() - 1);
        }
    }


    class RepositoryViewHolder extends BaseViewHolder {
        private final ViewDataBinding binding;
        private ImageView ivPhoto;

        RepositoryViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            ivPhoto = itemView.findViewById(R.id.iv_owner_photo);

        }

        @Override
        public void onBind(int position) {
            super.onBind(position);

            Glide.with(ivPhoto).load(getItem(position).getOwner().getPhotoUrl()).into(ivPhoto);
            binding.setVariable(BR.repository, getItem(position));
            binding.setVariable(BR.viewModel, viewModel);
        }
    }

    public class ProgressHolder extends BaseViewHolder {
        ProgressHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
