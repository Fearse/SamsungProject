package com.example.projectsamsung;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;

public class ListOfVariantsFragment extends Fragment {
    private EditText searchProduct;
    private boolean allrecipes=false;
    private Button addProduct;
    private  Button addIngredient;
    private Spinner sort;
    private Spinner category;
    private DatabaseReference dataBase;
    private ArrayList<ProductFragment> products=new ArrayList<>();;
    private String USER_KEY = "Product";
    private String code;
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
        }
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Product product = ds.getValue(Product.class);
                    assert product != null;
                    boolean flag = true;
                    String tempCode = product.getCode();
                    if(!allrecipes)
                    for (int j = 0; j < tempCode.length(); j++) {
                        if (tempCode.charAt(j) <
                                code.charAt(j))
                            flag = false;
                    }
                    if (flag&&getFragmentManager() != null) {
                        ProductFragment productFragment=new ProductFragment(product);
                        products.add(productFragment);
                        getFragmentManager().beginTransaction().add(R.id.list,productFragment).commit();
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
        sort=view.findViewById(R.id.sorts);
        sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 0:
                        sortByAlphabetDown();
                        break;
                    case 1:
                        sortByAlphabetUp();
                        break;
                    case 2:
                        sortByTimeUp();
                        break;
                    case 3:
                        sortByTimeDown();
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        TextWatcher inputTw=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text=searchProduct.getText().toString();
                for (int i=0;i<products.size();i++)
                    if(products.get(i).getName().length()<text.length()
                            ||!(products.get(i).getName().substring(0,text.length()).equals(text)))
                        products.get(i).setGone();
                    else
                        products.get(i).setVisible();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        searchProduct=view.findViewById(R.id.searchProduct);
        searchProduct.addTextChangedListener(inputTw);
    }
    @SuppressLint("NewApi")
    public void sortByTimeDown()
    {
        for (int i=0;i<products.size();i++)
        {
            getFragmentManager().beginTransaction().detach(products.get(i)).commit();
        }
        products.sort(new Comparator<ProductFragment>() {
            @Override
            public int compare(ProductFragment o1, ProductFragment o2) {
                if(o1.getTime() <= o2.getTime()) {
                    return 1;
                }
                //   else if(o1.getName().charAt(0)<o2.getName().charAt(0))
                return -1;
                //  else return 0;
            }
        });
        for (int i=0;i<products.size();i++)
        {
            getFragmentManager().beginTransaction().attach(products.get(i)).commit();
        }
    }
    @SuppressLint("NewApi")
    public void sortByTimeUp()
    {
        for (int i=0;i<products.size();i++)
        {
            getFragmentManager().beginTransaction().detach(products.get(i)).commit();
        }
        products.sort(new Comparator<ProductFragment>() {
            @Override
            public int compare(ProductFragment o1, ProductFragment o2) {
                if(o1.getTime() >= o2.getTime()) {
                    return 1;
                }
                //   else if(o1.getName().charAt(0)<o2.getName().charAt(0))
                return -1;
                //  else return 0;
            }
        });
        for (int i=0;i<products.size();i++)
        {
            getFragmentManager().beginTransaction().attach(products.get(i)).commit();
        }
    }
    @SuppressLint("NewApi")
    public void sortByAlphabetDown()
    {
        for (int i=0;i<products.size();i++)
        {
            getFragmentManager().beginTransaction().detach(products.get(i)).commit();
        }
        products.sort(new Comparator<ProductFragment>() {
            @Override
            public int compare(ProductFragment o1, ProductFragment o2) {
                if(o1.getName().charAt(0)>=o2.getName().charAt(0))
                    return 1;
                //   else if(o1.getName().charAt(0)<o2.getName().charAt(0))
                return -1;
                //  else return 0;
            }
        });
        for (int i=0;i<products.size();i++)
        {
            getFragmentManager().beginTransaction().attach(products.get(i)).commit();
        }
    }
    @SuppressLint("NewApi")
    public void sortByAlphabetUp()
    {
        for (int i=0;i<products.size();i++)
        {
            getFragmentManager().beginTransaction().detach(products.get(i)).commit();
        }
        products.sort(new Comparator<ProductFragment>() {
            @Override
            public int compare(ProductFragment o1, ProductFragment o2) {
                if(o1.getName().charAt(0)<=o2.getName().charAt(0))
                    return 1;
             //   else if(o1.getName().charAt(0)<o2.getName().charAt(0))
                    return -1;
              //  else return 0;
            }
        });
        for (int i=0;i<products.size();i++)
        {
            getFragmentManager().beginTransaction().attach(products.get(i)).commit();
        }
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
