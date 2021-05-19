package com.example.projectsamsung;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChooseFragment extends Fragment {
    private Button homeButton;
    private Button continueButton;
    private ArrayList<String> ings=new ArrayList<>();
    private EditText search;
    private boolean makeProduct=false;
    private DatabaseReference database;
    private MakeProductFragment makeProductFragment;
    private String key = "Ingredient";
    private ArrayList<IngredientFragment> ingredientFragments=new ArrayList<>();
    private String code="";
    ChooseFragment(){makeProduct=false;};
   ChooseFragment(MakeProductFragment makeProductFragment) {
       this.makeProductFragment=makeProductFragment;
       makeProduct=true;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ChooseFragmentView = inflater.inflate(R.layout.choose_fragment, container, false);
        findButton(ChooseFragmentView);
        createSwitch(ChooseFragmentView);
        return ChooseFragmentView;
    }
    void createSwitch(View view)
    {
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
    void findButton(View view){
        homeButton=(Button) view.findViewById(R.id.homeButton);
        if(makeProduct)
            homeButton.setVisibility(View.GONE);
        else
            homeButton.setVisibility(View.VISIBLE);
        continueButton = (Button) view.findViewById(R.id.continueButton);
        TextWatcher inputTw=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text=search.getText().toString();
                for (int i=0;i<ingredientFragments.size();i++)
                    if(ingredientFragments.get(i).getName().length()<text.length()
                            ||!(ingredientFragments.get(i).getName().substring(0,text.length()).equals(text)))
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
        homeButton.setOnClickListener(homeButtonClick);
        continueButton.setOnClickListener(continueButtonClick);
        database= FirebaseDatabase.getInstance().getReference(key);
    }
    View.OnClickListener homeButtonClick=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            StartFragment startFragment=new StartFragment();
            getFragmentManager().beginTransaction().replace(R.id.main,startFragment).commit();
        }
    };
    View.OnClickListener continueButtonClick=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!makeProduct) {
                checkSwitch();
                ListOfVariantsFragment listOfVariantsFragment = new ListOfVariantsFragment(code);
                getFragmentManager().beginTransaction().replace(R.id.main, listOfVariantsFragment).commit();
            }
            else
            {
                checkSwitch();
                makeProductFragment.setCode(code);
                makeProductFragment.setIngs(ings);
                getFragmentManager().beginTransaction().show(makeProductFragment).commit();
                makeProductFragment.setIngredientReady();
                getFragmentManager().beginTransaction().detach(getInstance()).commit();
            }
        }
    };
    public ChooseFragment getInstance()
    {
        return this;
    }
    void checkSwitch()
    {
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
