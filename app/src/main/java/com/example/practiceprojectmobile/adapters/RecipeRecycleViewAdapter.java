package com.example.practiceprojectmobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practiceprojectmobile.R;
import com.example.practiceprojectmobile.models.CrudOperationType;
import com.example.practiceprojectmobile.models.Ingredient;
import com.example.practiceprojectmobile.models.Recipe;
import com.example.practiceprojectmobile.models.User;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class RecipeRecycleViewAdapter extends RecyclerView.Adapter<RecipeRecycleViewAdapter.ViewHolder> {

    private List<Recipe> recipesList;
    private int userId;
    private List<User> users;
    private OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onItemClick(int recipeId, CrudOperationType operationType);

    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public RecipeRecycleViewAdapter(List<Recipe> recipesList,int userId,List<User> users) {
        this.recipesList = recipesList;
        this.userId = userId;
        this.users = users;
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
        holder.id = recipe.getId();
        if (userId==recipe.getAuthorId()){
            holder.recipeItemDeleteButton.setVisibility(View.VISIBLE);
            holder.recipeItemEditButton.setVisibility(View.VISIBLE);
            holder.recipeItemEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(holder.id,CrudOperationType.EDIT);
                    }
                }
            });
            holder.recipeItemDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(holder.id,CrudOperationType.DELETE);
                    }
                }
            });
        }
//        holder.recipeDescription= recipe.getDescription();
        StringBuilder ingredientString = new StringBuilder();
//        for (Ingredient ingredient:recipe.getIngredients()) {
//            ingredientString.append(ingredient.getName()).append(" : ").append(ingredient.getQuantity()).append("\n");
//        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        for (User user:users) {
            if (user.getId()== recipe.getAuthorId()){
                holder.recipeAuthorField.setText("Author: " + user.getUsername());
                break;
            }
        }
        holder.recipeDateCreatedField.setText(dateFormat.format(recipe.getDateCreated()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call the onItemClick method on the OnItemClickListener
                if (mListener != null) {
                    mListener.onItemClick(holder.id,CrudOperationType.READ);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        int id;
        AppCompatButton recipeItemDeleteButton;
        AppCompatButton recipeItemEditButton;
        TextView recipeNameField;
//        String recipeDescription;
        TextView recipeDateCreatedField;
        TextView recipeAuthorField;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeNameField=itemView.findViewById(R.id.recipeNameField);
            recipeDateCreatedField=itemView.findViewById(R.id.recipeDateCreatedField);
            recipeAuthorField=itemView.findViewById(R.id.recipeAuthorField);
            recipeItemDeleteButton = itemView.findViewById(R.id.recipeItemDeleteButton);
            recipeItemEditButton = itemView.findViewById(R.id.recipeItemEditButton);


        }

    }
    public void updateRecipes(List<Recipe> recipes){
        this.recipesList = recipes;
        notifyDataSetChanged();
    }
}