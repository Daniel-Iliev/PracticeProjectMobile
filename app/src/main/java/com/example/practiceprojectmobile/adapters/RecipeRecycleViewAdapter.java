package com.example.practiceprojectmobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practiceprojectmobile.R;
import com.example.practiceprojectmobile.models.Recipe;

import java.util.List;

public class RecipeRecycleViewAdapter extends RecyclerView.Adapter<RecipeRecycleViewAdapter.ViewHolder> {

    private List<Recipe> recipesList;

    public RecipeRecycleViewAdapter(List<Recipe> recipesList) {
        this.recipesList = recipesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipes_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = recipesList.get(position);
        holder.recipeNameField.setText(recipe.getName());
    }

    @Override
    public int getItemCount() {
        return recipesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView recipeNameField;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeNameField=itemView.findViewById(R.id.recipeNameField);


        }

    }
}