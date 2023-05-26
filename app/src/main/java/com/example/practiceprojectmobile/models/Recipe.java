package com.example.practiceprojectmobile.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Date;

public class Recipe {
    private int id;
    private String name;
    private Collection<Ingredient> ingredients;
    private int authorId;
    private int minutesNeeded;
    private String description;
    private Date dateCreated;

    public Recipe(int id, String name, Collection<Ingredient> ingredients, int authorId, int minutesNeeded, String description, Date dateCreated) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.authorId = authorId;
        this.minutesNeeded = minutesNeeded;
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

    public int getMinutesNeeded() {
        return minutesNeeded;
    }

    public void setMinutesNeeded(int minutesNeeded) {
        this.minutesNeeded = minutesNeeded;
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
    public JSONObject convertToJSONObject(Boolean withOrWithoutId,Boolean withOrWithoutDateCreated) throws JSONException {
        JSONObject recipe = new JSONObject();
        if (withOrWithoutId){
            recipe.put("recipe-id",this.id);
        }
        if (withOrWithoutDateCreated){
            recipe.put("recipe-date-created",this.getDateCreated());
        }
        recipe.put("recipe-name",this.name);
        recipe.put("recipe-duration",this.minutesNeeded);
        recipe.put("recipe-description",this.description);
        recipe.put("recipe-author-id",this.authorId);
        JSONArray ingredients = new JSONArray();
        for (Ingredient ingredient:this.ingredients) {
            JSONObject ingredientObj = new JSONObject();
            ingredientObj.put("ingredient-name",ingredient.getName());
            ingredientObj.put("ingredient-quantity",ingredient.getQuantity());
            ingredients.put(ingredientObj);
        }
        recipe.put("recipe-ingredients",ingredients);
        return recipe;
    }
}
