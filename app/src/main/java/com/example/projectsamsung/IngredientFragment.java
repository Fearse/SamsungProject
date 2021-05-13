package com.example.projectsamsung;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class IngredientFragment extends Fragment {
    public String name;
    private TextView nameIng;
    private CheckBox checkIng;
    IngredientFragment(String name)
    {
        this.name=name;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ingredientView = inflater.inflate(R.layout.ingredient_fragment, container, false);
        ingredientView.setOnClickListener(ingredientViewClick);
        init(ingredientView);
        return ingredientView;
    }
    private void init(View view)
    {
        nameIng=view.findViewById(R.id.ingredientName);
        nameIng.setText(name);
        checkIng=view.findViewById(R.id.ingredientCheck);
    }
    public boolean getChecked()
    {
        return checkIng.isChecked();
    }
    public String getName()
    {
        return name;
    }
    public void setGone()
    {
        nameIng.setVisibility(View.GONE);
        checkIng.setVisibility(View.GONE);
    }
    public void setVisible()
    {
        nameIng.setVisibility(View.VISIBLE);
        checkIng.setVisibility(View.VISIBLE);
    }
    View.OnClickListener ingredientViewClick=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(checkIng.isChecked())
                checkIng.setChecked(true);
            else
                checkIng.setChecked(false);
        }
    };
}
