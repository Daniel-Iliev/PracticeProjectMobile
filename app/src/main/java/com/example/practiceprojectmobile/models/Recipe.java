package com.example.practiceprojectmobile.models;

import java.util.Collection;
import java.util.Date;

public class Recipe {
    private int id;
    private String name;
    private Collection<Ingredient> ingredients;
    private int authorId;
    private int duration;
    private String description;
    private Date dateCreated;

    public Recipe(int id, String name, Collection<Ingredient> ingredients, int authorId, int duration, String description, Date dateCreated) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.authorId = authorId;
        this.duration = duration;
        this.description = description;
        this.dateCreated = dateCreated;
    }

    public Recipe() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Collection<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
