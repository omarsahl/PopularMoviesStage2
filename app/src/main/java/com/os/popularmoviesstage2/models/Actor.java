package com.os.popularmoviesstage2.models;

import com.google.gson.annotations.SerializedName;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by Omar on 01-Mar-18 8:04 PM
 */

@Entity
public class Actor {
    @Id(assignable = true)
    @SerializedName("id")
    private long id;

    @SerializedName("character")
    private String character;

    @SerializedName("name")
    private String name;

    @SerializedName("profile_path")
    private String profileImage;

    @SerializedName("order")
    private int order;

    public Actor() {}

    public Actor(long id, String character, String name, String profileImage, int order) {
        this.id = id;
        this.character = character;
        this.name = name;
        this.profileImage = profileImage;
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
