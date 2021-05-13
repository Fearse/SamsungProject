package com.example.projectsamsung;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListOfVariantsFragment extends Fragment {
    boolean allrecipes=false;
    private Button addProduct;
    private  Button addIngredient;
    private DatabaseReference dataBase;
    private int countIng=15;
    private ArrayList<Product> products;
    private String USER_KEY = "Product";
    String code;
    ListOfVariantsFragment(boolean allrecipes)
    {
        this.allrecipes=allrecipes;
    }
    ListOfVariantsFragment(String code)
    {
        this.code=code;
    }
    private Button homeButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ListOfVariantsView = inflater.inflate(R.layout.list_of_variants_fragment, container, false);
        findButtons(ListOfVariantsView);
        checkRecipes();
        return ListOfVariantsView;
    }
    void checkRecipes() {
        if (allrecipes == false) {
            addProduct.setVisibility(View.GONE);
            addIngredient.setVisibility(View.GONE);
        } else {
            code = "";
            for (int i = 0; i < countIng; i++)
                code += "0";
        }
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Product product = ds.getValue(Product.class);
                    assert product != null;
                    boolean flag = true;
                    String tempCode = product.getCode();
                    for (int j = 0; j < tempCode.length(); j++) {
                        if (tempCode.charAt(j) <
                                code.charAt(j))
                            flag = false;
                    }
                    if (flag&&getFragmentManager() != null) {
                        getFragmentManager().beginTransaction().add(R.id.list,new ProductFragment(product)).commit();
                    }
                }
            }
            @Override
            public void onCancelled (@NonNull DatabaseError error){
            }
        };
        dataBase.addValueEventListener(vListener);

    }
    void findButtons(View view)
    {
        homeButton=(Button)view.findViewById(R.id.homeButton);
        homeButton.setOnClickListener(homeButtonClick);
        addProduct=(Button)view.findViewById(R.id.addProduct);
        addProduct.setOnClickListener(addProductClick);
        addIngredient=(Button)view.findViewById(R.id.addIngredient);
        addIngredient.setOnClickListener(addIngredientClick);
        dataBase= FirebaseDatabase.getInstance().getReference(USER_KEY);
    }
    View.OnClickListener addProductClick=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getFragmentManager().beginTransaction().replace(R.id.main,new MakeProductFragment()).commit();
        }
    };
    View.OnClickListener addIngredientClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getFragmentManager().beginTransaction().replace(R.id.main,new MakeIngredientFragment()).commit();
        }
    };
    View.OnClickListener homeButtonClick=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getFragmentManager().beginTransaction().replace(R.id.main,new StartFragment()).commit();
        }
    };
}
