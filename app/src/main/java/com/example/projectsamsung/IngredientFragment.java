package com.example.projectsamsung;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class IngredientFragment extends Fragment {
    public String name;
    private TextView nameIng;
    private CheckBox checkIng;
    private LinearLayout ingLinear;

    IngredientFragment(String name)
    {
        this.name=name;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ingredientView = inflater.inflate(R.layout.ingredient_fragment, container, false);
        init(ingredientView);
        return ingredientView;
    }
    private void init(View view) {
        nameIng=view.findViewById(R.id.ingredientName);
        nameIng.setText(name);
        ingLinear=view.findViewById(R.id.ingLinear);
        ingLinear.setOnClickListener(ingredientViewClick);
        checkIng=view.findViewById(R.id.ingredientCheck);
    }
    public boolean getChecked() {
        return checkIng.isChecked();
    }
    public String getName() {
        return name;
    }
    public void setGone() {
        ingLinear.setVisibility(View.GONE);
        nameIng.setVisibility(View.GONE);
        checkIng.setVisibility(View.GONE);
    }
    public void setVisible() {
        ingLinear.setVisibility(View.VISIBLE);
        nameIng.setVisibility(View.VISIBLE);
        checkIng.setVisibility(View.VISIBLE);
    }
    View.OnClickListener ingredientViewClick=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(checkIng.isChecked())
                checkIng.setChecked(false);
            else
                checkIng.setChecked(true);
        }
    };
}
