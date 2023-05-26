package com.example.practiceprojectmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.practiceprojectmobile.adapters.RecipeRecycleViewAdapter;
import com.example.practiceprojectmobile.models.CrudOperationType;
import com.example.practiceprojectmobile.models.Ingredient;
import com.example.practiceprojectmobile.models.Recipe;
import com.example.practiceprojectmobile.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainScreen extends AppCompatActivity {
    private String authToken;
    private List<Recipe> recipes;
    private String recipesApiEndpoint;
    private AppCompatButton addRecipeButton;
    private RecipeRecycleViewAdapter recipeRecycleViewAdapter;
    private Dialog recipeInfoPopup;
    private Dialog recipeFormPopup;
    private String usersEndpoint;
    private User currentUser;
    private List<User> allUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instantiate();
        getCurrentUserInfoFromDb();
    }

    private void getUsersFromDb() {
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET,usersEndpoint,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        allUsers = new Gson().fromJson(response, new TypeToken<List<User>>(){}.getType());
                        instantiateRecipeRecyclerView();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainScreen.this,error.toString(),Toast.LENGTH_LONG).show();
            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer "+ authToken);
                return params;
            }
        };
        mRequestQueue.add(request);
    }

    private void getCurrentUserInfoFromDb() {
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET,usersEndpoint + "/info",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        currentUser = new Gson().fromJson(response,User.class);
                        getUsersFromDb();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainScreen.this,error.toString(),Toast.LENGTH_LONG).show();
            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer "+ authToken);
                return params;
            }
        };
        mRequestQueue.add(request);
    }

    public void instantiate(){
        recipesApiEndpoint ="https://practice-api-service-practice-api-recipes.azuremicroservices.io/recipe";
        usersEndpoint = "https://practice-api-service-practice-api-recipes.azuremicroservices.io/user";
        authToken=this.getIntent().getExtras().getString("authToken");
        recipes = new ArrayList<>();
        recipeInfoPopup = new Dialog(MainScreen.this);
        recipeFormPopup = new Dialog(MainScreen.this);
        addRecipeButton = findViewById(R.id.addRecipeButton);
        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecipeFormPopup(CrudOperationType.READ,0);
                recipeRecycleViewAdapter.updateRecipes(recipes);
            }
        });
    }

    private void showRecipeFormPopup(CrudOperationType operationType, int recipeId) {
        recipeFormPopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        recipeFormPopup.setCancelable(true);
        recipeFormPopup.setContentView(R.layout.recipe_form_popup);
        recipeFormPopup.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                recipeFormPopup = new Dialog(MainScreen.this);
            }
        });
        recipeFormPopup.show();
        EditText recipeFormNameField = recipeFormPopup.findViewById(R.id.recipeFormNameField);
        EditText recipeFormDescriptionField = recipeFormPopup.findViewById(R.id.recipeFormDescriptionField);
        EditText recipeFormMinutesNeededField = recipeFormPopup.findViewById(R.id.recipeFormMinutesNeededField);
        EditText addIngredientNameField = recipeFormPopup.findViewById(R.id.addIngredientNameField);
        EditText addIngredientQuantityField = recipeFormPopup.findViewById(R.id.addIngredientQuantityField);
        AppCompatButton addCurrentIngredientButton = recipeFormPopup.findViewById(R.id.addCurrentIngredientButton);

        List<Ingredient> ingredients = new ArrayList<>();
        addCurrentIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ingredient ingredient = new Ingredient(addIngredientNameField.getText().toString(),addIngredientQuantityField.getText().toString());
                ingredients.add(ingredient);
            }
        });
        Recipe recipe = new Recipe();
        if (operationType.equals(CrudOperationType.EDIT)){
            TextView recipeFormFixedHeading = recipeFormPopup.findViewById(R.id.recipeFormFixedHeading);
            recipeFormFixedHeading.setText("Edit Recipe");
            if (recipeId!=0){
                Recipe editRecipe = new Recipe();
                for (Recipe tempRecipe:recipes) {
                    if (tempRecipe.getId()==recipeId){
                        editRecipe = tempRecipe;
                        break;
                    }
                }
                recipe.setId(recipeId);
                recipe.setDateCreated(editRecipe.getDateCreated());
                recipeFormNameField.setText(editRecipe.getName());
                recipeFormMinutesNeededField.setText(String.valueOf(editRecipe.getMinutesNeeded()));
                recipeFormDescriptionField.setText(editRecipe.getDescription());
            }
        }
        AppCompatButton submitRecipeButton = recipeFormPopup.findViewById(R.id.submitRecipeButton);
        if (operationType.equals(CrudOperationType.EDIT)){
            submitRecipeButton.setText("Update Recipe");
        }
        submitRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipe.setName(recipeFormNameField.getText().toString());
                recipe.setMinutesNeeded(Integer.parseInt(recipeFormMinutesNeededField.getText().toString()));
                recipe.setDescription(recipeFormDescriptionField.getText().toString());
                recipe.setIngredients(ingredients);
                try {
                    saveRecipeToDb(recipe,operationType,recipeId);
                    recipeFormPopup.cancel();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void showRecipeInfoPopup(int recipeId){
        recipeInfoPopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        recipeInfoPopup.setCancelable(true);
        recipeInfoPopup.setContentView(R.layout.recipe_info_popup);
        TextView recipeNameField = recipeInfoPopup.findViewById(R.id.recipeNameField);
        TextView recipeMinutesNeededField = recipeInfoPopup.findViewById(R.id.recipeMinutesNeededField);
        TextView recipeIngredientsField = recipeInfoPopup.findViewById(R.id.recipeIngredientsField);
        TextView recipeDescriptionField = recipeInfoPopup.findViewById(R.id.recipeDescriptionField);
        TextView recipeAuthorField = recipeInfoPopup.findViewById(R.id.recipeAuthorField);
        TextView recipeDateCreatedField = recipeInfoPopup.findViewById(R.id.recipeDateCreatedField);
        Recipe recipe = new Recipe();
        for (Recipe tempRecipe:recipes) {
            if (tempRecipe.getId()==recipeId){
                recipe = tempRecipe;
                break;
            }
        }
        for (User user:allUsers) {
            if (user.getId()==recipe.getAuthorId()){
                recipeAuthorField.setText(user.getUsername());
            }

        }
        recipeNameField.setText(recipe.getName());
        recipeDescriptionField.setText(recipe.getDescription());
        StringBuilder ingredientsString = new StringBuilder();
        for (Ingredient ingredient:recipe.getIngredients()) {
            ingredientsString.append(ingredient.getName() + " - " + ingredient.getQuantity()+ "\n");
        }
        recipeIngredientsField.setText(ingredientsString);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        recipeDateCreatedField.setText(sdf.format(recipe.getDateCreated()));
        recipeMinutesNeededField.setText(String.valueOf(recipe.getMinutesNeeded()));
        recipeInfoPopup.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                recipeInfoPopup = new Dialog(MainScreen.this);
            }
        });
        recipeInfoPopup.show();
    }
    public void instantiateRecipeRecyclerView(){
        RecyclerView recipesRecyclerView = findViewById(R.id.recipesContainer);
        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeRecycleViewAdapter =new RecipeRecycleViewAdapter(recipes, currentUser.getId(),allUsers);
        recipesRecyclerView.setAdapter(recipeRecycleViewAdapter);
        recipesSetOnItemClickListener();
        try {
            getRecipesFromDb();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    public void getRecipesFromDb() throws JSONException {
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET,recipesApiEndpoint,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        recipes.clear();
                        try {
                            JSONArray array = new JSONArray(response);
                           for (int i = 0;i<array.length();i++){
                               Recipe recipe = convertRecipeFromJson(array.getJSONObject(i));
                               recipes.add(recipe);
                           }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        recipeRecycleViewAdapter.updateRecipes(recipes);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainScreen.this,error.toString(),Toast.LENGTH_LONG).show();
            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer "+ authToken);
                return params;
            }
        };
        mRequestQueue.add(request);

    }
    public Recipe convertRecipeFromJson(JSONObject recipeObj) throws JSONException, ParseException {
        Recipe recipe = new Recipe();
        recipe.setName(recipeObj.getString("recipe-name"));
        recipe.setId(recipeObj.getInt("recipe-id"));
        recipe.setMinutesNeeded(recipeObj.getInt("recipe-minutes-needed"));
        recipe.setDescription(recipeObj.getString("recipe-description"));
        recipe.setAuthorId(recipeObj.getInt("recipe-author-id"));
        JSONArray ingredientsArr = recipeObj.getJSONArray("recipe-ingredients");
        List<Ingredient> ingredients = new ArrayList<>();
        for (int i = 0;i<ingredientsArr.length();i++){
            JSONObject ingredientObject = ingredientsArr.getJSONObject(i);
            Ingredient ingredient = new Ingredient(ingredientObject.getString("ingredient-name"),ingredientObject.getString("ingredient-quantity"));
            ingredients.add(ingredient);
        }
        recipe.setIngredients(ingredients);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX",Locale.ENGLISH);
        Date date = dateFormat.parse(recipeObj.getString("recipe-date-created"));
        recipe.setDateCreated(date);
        return recipe;
    }
    public void saveRecipeToDb(Recipe recipe, CrudOperationType operationType, int recipeId) throws JSONException {
        String dataEndpoint =recipesApiEndpoint;
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        JSONObject jsonParams = new JSONObject();
        JSONArray ingredientsArr = new JSONArray();
        jsonParams.put("recipe-name", recipe.getName());
        jsonParams.put("recipe-author-id", currentUser.getId());
        jsonParams.put("recipe-minutes-needed", recipe.getMinutesNeeded());
        jsonParams.put("recipe-description", recipe.getDescription());
        for (Ingredient ingredient:recipe.getIngredients()) {
            JSONObject ingredientObj = new JSONObject();
            ingredientObj.put("ingredient-name",ingredient.getName());
            ingredientObj.put("ingredient-quantity",ingredient.getQuantity());
            ingredientsArr.put(ingredientObj);
        }
        jsonParams.put("recipe-ingredients",ingredientsArr);
        Integer requestMethod = Request.Method.POST;
        if (operationType.equals(CrudOperationType.EDIT)){
            requestMethod = Request.Method.PUT;
            dataEndpoint+="/"+recipeId;
        }
        JsonObjectRequest request = new JsonObjectRequest(requestMethod,dataEndpoint,jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String responseStr = String.valueOf(response);
                        try {
                            getRecipesFromDb();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        recipeRecycleViewAdapter.updateRecipes(recipes);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainScreen.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+ authToken);
                return params;
            }

        }
        ;
        mRequestQueue.add(request);
    }
    private void recipesSetOnItemClickListener() {
        recipeRecycleViewAdapter.setOnItemClickListener(new RecipeRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int recipeId, CrudOperationType operationType) {
                if (operationType.equals(CrudOperationType.READ)){

                    showRecipeInfoPopup(recipeId);
                }
                else if (operationType.equals(CrudOperationType.DELETE)) {
                    showDeleteRecipePopup(recipeId);
                }
                else if (operationType.equals(CrudOperationType.EDIT)) {
                    showRecipeFormPopup(CrudOperationType.EDIT,recipeId);
                }
            }
        });
    }

    private void showDeleteRecipePopup(int recipeId) {

        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.DELETE,recipesApiEndpoint+"/"+recipeId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainScreen.this,"Delete Successful!",Toast.LENGTH_LONG).show();
                        try {
                            getRecipesFromDb();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainScreen.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+ authToken);
                return params;
            }

        }
                ;
        mRequestQueue.add(request);
    }

}