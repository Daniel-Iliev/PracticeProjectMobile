package com.example.practiceprojectmobile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.practiceprojectmobile.adapters.RecipeRecycleViewAdapter;
import com.example.practiceprojectmobile.models.Ingredient;
import com.example.practiceprojectmobile.models.Recipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe(1, "asd", null,1,1,"da",new Date()));
        recipes.add(new Recipe(2, "das", null,1,1,"da",new Date()));
        RecyclerView recipesRecycleView = findViewById(R.id.recipesContainer);
        recipesRecycleView.setLayoutManager(new LinearLayoutManager(this));
        recipesRecycleView.setAdapter(new RecipeRecycleViewAdapter(recipes));
    }

}