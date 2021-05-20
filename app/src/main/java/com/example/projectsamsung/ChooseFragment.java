package com.example.projectsamsung;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChooseFragment extends Fragment {
    private final ArrayList<String> ings=new ArrayList<>();
    private EditText search;
    private boolean makeProduct=false;
    private DatabaseReference database;
    private MakeProductFragment makeProductFragment;
    private final ArrayList<IngredientFragment> ingredientFragments=new ArrayList<>();
    private String code="";
    private LinearLayout listofIng;

    ChooseFragment(){makeProduct=false;}
    ChooseFragment(MakeProductFragment makeProductFragment) {
       this.makeProductFragment=makeProductFragment;
       makeProduct=true;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ChooseFragmentView = inflater.inflate(R.layout.choose_fragment, container, false);
        init(ChooseFragmentView);
        createIngs();
        return ChooseFragmentView;
    }
    public void createIngs() {
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ingredientFragments.clear();
                for(int i=0;i<listofIng.getChildCount();i++)
                    listofIng.getChildAt(i).setVisibility(View.GONE);
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String ingredientName=ds.getValue(String.class);
                    assert ingredientName != null;
                    if (getFragmentManager() != null) {
                        IngredientFragment ingredientFragment=new IngredientFragment(ingredientName);
                        ingredientFragments.add(ingredientFragment);
                        getFragmentManager().beginTransaction().add(R.id.ingredientList,ingredientFragment).commit();
                    }
                }
            }
            @Override
            public void onCancelled (@NonNull DatabaseError error){
            }
        };
        database.addValueEventListener(vListener);
    }
    public void init(View view){

        listofIng=view.findViewById(R.id.ingredientList);

        Button homeButton = view.findViewById(R.id.homeButton);
        homeButton.setOnClickListener(homeButtonClick);
        if(makeProduct)
            homeButton.setVisibility(View.GONE);
        else
            homeButton.setVisibility(View.VISIBLE);

        Button continueButton = view.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(continueButtonClick);

        TextWatcher inputTw=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text=search.getText().toString().toLowerCase();
                for (int i=0;i<ingredientFragments.size();i++)
                    if(ingredientFragments.get(i).getName().length()<text.length()
                            ||(!ingredientFragments.get(i).getName().toLowerCase().contains(text)))
                        ingredientFragments.get(i).setGone();
                    else
                        ingredientFragments.get(i).setVisible();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        search=view.findViewById(R.id.searchIngredient);
        search.addTextChangedListener(inputTw);

        String key = "Ingredient";
        database= FirebaseDatabase.getInstance().getReference(key);
    }

    View.OnClickListener homeButtonClick= view -> {
        StartFragment startFragment=new StartFragment();
        getFragmentManager().beginTransaction().replace(R.id.main,startFragment).commit();
    };
    View.OnClickListener continueButtonClick=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!makeProduct) {
                checkIngs();
                ListOfVariantsFragment listOfVariantsFragment = new ListOfVariantsFragment(code);
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction().replace(R.id.main, listOfVariantsFragment).commit();
            }
            else
            {
                checkIngs();
                makeProductFragment.setCode(code);
                makeProductFragment.setIngs(ings);
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction().show(makeProductFragment).commit();
                makeProductFragment.setIngredientReady();
                getFragmentManager().beginTransaction().detach(getInstance()).commit();
            }
        }
    };
    public ChooseFragment getInstance() {
        return this;
    }
    public void checkIngs(){
        if(!makeProduct) {
            for (int i = 0; i < ingredientFragments.size(); i++) {
                if (ingredientFragments.get(i).getChecked())
                    code += "1";
                else code += "0";
            }
        }
        else
        {
            for (int i = 0; i < ingredientFragments.size(); i++) {
                if (ingredientFragments.get(i).getChecked()) {
                    code += "1";
                    ings.add(ingredientFragments.get(i).getName());
                }
                    else code += "0";
            }
        }
    }
}
