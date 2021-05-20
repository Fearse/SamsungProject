package com.example.projectsamsung;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MakeIngredientFragment extends Fragment {
    private EditText nameEd;
    private DatabaseReference database;
    ArrayList<IngredientFragment> ingredientFragments=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View makeIngredientView = inflater.inflate(R.layout.make_ingredient_fragment, container, false);
        init(makeIngredientView);
        return makeIngredientView;
    }

    public void init(View view) {
        String KEY="Ingredient";
        nameEd = view.findViewById(R.id.makeIngredientName);
        Button readyButton = view.findViewById(R.id.makeIngredientButton);
        readyButton.setOnClickListener(readyButtonClick);
        Button homeButton = view.findViewById(R.id.makeIngHomeButton);
        homeButton.setOnClickListener(homeButtonClick);
        database = FirebaseDatabase.getInstance().getReference(KEY);

        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ingredientFragments.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String ingredientName=ds.getValue(String.class);
                    assert ingredientName != null;
                    if (getFragmentManager() != null) {
                        IngredientFragment ingredientFragment=new IngredientFragment(ingredientName);
                        ingredientFragments.add(ingredientFragment);
                    }
                }
            }
            @Override
            public void onCancelled (@NonNull DatabaseError error){
            }
        };
        database.addValueEventListener(vListener);
    }
    View.OnClickListener homeButtonClick= view -> {
        assert getFragmentManager() != null;
        getFragmentManager().beginTransaction().replace(R.id.main,new ListOfVariantsFragment(true)).commit();
    };
    public boolean checkForUnique(String text)
    {
        for (int i=0;i<ingredientFragments.size();i++)
        {
            if (text.toLowerCase().equals(ingredientFragments.get(i).getName().toLowerCase()))
                return false;
        }
        return true;
    }
    View.OnClickListener readyButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String name = nameEd.getText().toString();
            if(!name.equals("")&&checkForUnique(name)) {
                database.push().setValue(name);
                Toast.makeText(getContext(), "Добавлено", Toast.LENGTH_SHORT).show();
            }
            if(name.equals(""))
                Toast.makeText(getContext(), "Введите что-нибудь", Toast.LENGTH_SHORT).show();
            else Toast.makeText(getContext(), "Введённый игредиент уже существует", Toast.LENGTH_SHORT).show();
        }
    };
}
