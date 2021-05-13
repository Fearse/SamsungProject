package com.example.projectsamsung;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MakeIngredientFragment extends Fragment {
    private EditText nameEd;
    private Button readyButton;
    private DatabaseReference database;
    private String key = "Ingredient";
    private String name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View makeIngredientView = inflater.inflate(R.layout.make_ingredient_fragment, container, false);
        init(makeIngredientView);
        return makeIngredientView;
    }

    private void init(View view) {
        nameEd = view.findViewById(R.id.makeIngredientName);
        readyButton = view.findViewById(R.id.makeIngredientButton);
        readyButton.setOnClickListener(readyButtonClick);
        database = FirebaseDatabase.getInstance().getReference(key);
    }

    View.OnClickListener readyButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            name=nameEd.getText().toString();
            database.push().setValue(name);
        }

        ;
    };
}
