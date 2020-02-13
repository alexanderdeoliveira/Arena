package com.alexander.arenatest.model;

import androidx.databinding.BaseObservable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Owner extends BaseObservable implements Serializable {

    @JsonProperty("login")
    private String name;

    @JsonProperty("avatar_url")
    private String photoUrl;

    public void setName(String name) {
        this.name = name;
        this.photoUrl = null;
    }

    public String getName() {
        return name;
    }


    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
}
