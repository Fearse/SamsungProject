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
    private Button addIngredient;
    private DatabaseReference dataBase;
    private ArrayList<ProductFragment> products=new ArrayList<>();;
    private Spinner category;
    private String code;
    private LinearLayout list;

    ListOfVariantsFragment(boolean allrecipes)
    {
        this.allrecipes=allrecipes;
    }
    ListOfVariantsFragment(String code)
    {
        this.code=code;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ListOfVariantsView = inflater.inflate(R.layout.list_of_variants_fragment, container, false);
        init(ListOfVariantsView);
        checkRecipes();
        return ListOfVariantsView;
    }
    public void checkRecipes() {
        if (!allrecipes) {
            addProduct.setVisibility(View.GONE);
            addIngredient.setVisibility(View.GONE);
        }
        else code="";
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                products.clear();
                for(int i=0;i<list.getChildCount();i++)
                    list.getChildAt(i).setVisibility(View.GONE);
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Product product = ds.getValue(Product.class);
                    assert product != null;
                    boolean flag = true;
                    String tempCode = product.getCode();
                    if(!code.equals(""))
                    for (int j = 0; j < tempCode.length(); j++) {
                        if (tempCode.charAt(j) > code.charAt(j))
                            flag = false;
                    }
                    if (flag&&getFragmentManager() != null) {
                        ProductFragment productFragment=new ProductFragment(product);
                        productFragment.setListOfVariantsFragment(getInstance());
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
    public void init(View view) {
        String KEY = "Product";
        dataBase= FirebaseDatabase.getInstance().getReference(KEY);
        list=view.findViewById(R.id.list);

        Button homeButton = view.findViewById(R.id.homeButton);
        homeButton.setOnClickListener(homeButtonClick);
        addProduct = view.findViewById(R.id.addProduct);
        addProduct.setOnClickListener(addProductClick);
        addIngredient = view.findViewById(R.id.addIngredient);
        addIngredient.setOnClickListener(addIngredientClick);


        category=view.findViewById(R.id.categoriesSpinner);
        category.setSelection(0);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortByCategory(category.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner sort = view.findViewById(R.id.sorts);
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
                String text=searchProduct.getText().toString().toLowerCase();
                for (int i=0;i<products.size();i++)
                    if(products.get(i).getName().length()<text.length()
                            ||(!products.get(i).getName().toLowerCase().contains(text)))
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
    public void sortByTimeDown() {
        for (int i=0;i<products.size();i++) {
            assert getFragmentManager() != null;
            getFragmentManager().beginTransaction().detach(products.get(i)).commit();
        }
        products.sort((o1, o2) -> {
            if(o1.getTime() <= o2.getTime()) {
                return 1;
            }
            return -1;
        });
        for (int i=0;i<products.size();i++) {
            assert getFragmentManager() != null;
            getFragmentManager().beginTransaction().attach(products.get(i)).commit();
        }
    }

    @SuppressLint("NewApi")
    public void sortByTimeUp() {
        for (int i=0;i<products.size();i++) {
            assert getFragmentManager() != null;
            getFragmentManager().beginTransaction().detach(products.get(i)).commit();
        }
        products.sort((o1, o2) -> {
            if(o1.getTime() >= o2.getTime()) {
                return 1;
            }
            return -1;
        });
        for (int i=0;i<products.size();i++) {
            assert getFragmentManager() != null;
            getFragmentManager().beginTransaction().attach(products.get(i)).commit();
        }
    }

    public void sortByCategory(String categoryStr) {
        if (category.getSelectedItemPosition()!=0&&category.getSelectedItemPosition()!=1)
        for (int i=0;i<products.size();i++) {
            if ((products.get(i).getProduct().getCategory().equals(categoryStr)))
                products.get(i).setVisible();
            else products.get(i).setGone();
        }
        else if(category.getSelectedItemPosition()==1)
            for (int i=0;i<products.size();i++)
                products.get(i).setVisible();
    }

    @SuppressLint("NewApi")
    public void sortByAlphabetDown() {
        for (int i=0;i<products.size();i++) {
            assert getFragmentManager() != null;
            getFragmentManager().beginTransaction().detach(products.get(i)).commit();
        }
        products.sort((o1, o2) -> {
            if(o1.getName().charAt(0)>=o2.getName().charAt(0))
                return 1;
            return -1;
        });
        for (int i=0;i<products.size();i++) {
            assert getFragmentManager() != null;
            getFragmentManager().beginTransaction().attach(products.get(i)).commit();
        }
    }

    @SuppressLint("NewApi")
    public void sortByAlphabetUp() {
        for (int i=0;i<products.size();i++) {
            assert getFragmentManager() != null;
            getFragmentManager().beginTransaction().detach(products.get(i)).commit();
        }
        products.sort((o1, o2) -> {
            if(o1.getName().charAt(0)<=o2.getName().charAt(0))
                return 1;
            return -1;
        });
        for (int i=0;i<products.size();i++) {
            assert getFragmentManager() != null;
            getFragmentManager().beginTransaction().attach(products.get(i)).commit();
        }

    }

    public ListOfVariantsFragment getInstance() {
        return this;
    }
    public String getCode() {
        return code;
    }

    View.OnClickListener addProductClick= view -> getFragmentManager().beginTransaction().replace(R.id.main,new MakeProductFragment()).commit();
    View.OnClickListener addIngredientClick= v -> getFragmentManager().beginTransaction().replace(R.id.main,new MakeIngredientFragment()).commit();
    View.OnClickListener homeButtonClick= view -> getFragmentManager().beginTransaction().replace(R.id.main,new StartFragment()).commit();
}
