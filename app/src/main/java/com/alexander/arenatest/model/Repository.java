package com.alexander.arenatest.model;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.recyclerview.widget.DiffUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Repository extends BaseObservable implements Serializable {

    private long id;
    @JsonProperty("node_id")
    private String nodeId;
    private String name;
    private String description;
    private String forks;
    @JsonProperty("full_name")
    private String fullName;
    @JsonProperty("stargazers_count")
    private String stars;
    private Owner owner;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setForks(String forks) {
        this.forks = forks;
    }

    public String getForks() {
        return forks;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getStars() {
        return stars;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Owner getOwner() {
        return owner;
    }

    public static DiffUtil.ItemCallback<Repository> DIFF_CALLBACK = new DiffUtil.ItemCallback<Repository>() {
        @Override
        public boolean areItemsTheSame(@NonNull Repository oldItem, @NonNull Repository newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Repository oldItem, @NonNull Repository newItem) {
            return oldItem.equals(newItem);
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        Repository repository = (Repository) obj;
        return repository.id == this.id;
    }
}
