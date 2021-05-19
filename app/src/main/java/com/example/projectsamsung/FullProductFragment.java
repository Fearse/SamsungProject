package com.example.projectsamsung;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FullProductFragment extends Fragment {
    Button homeButton;
    TextView name;
    LinearLayout ingsLinear;
    LinearLayout recipeLinear;
    TextView time;
    ArrayList<String> ings;
    ImageView image;
    ArrayList<String> recipe;
    Product product;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    FullProductFragment(Product product)
    {
        this.product=product;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View FullProductView = inflater.inflate(R.layout.full_product_fragment, container, false);
        //  linearLayout.setBackgroundResource(getResources().getIdentifier(img,"drawable",getContext().getPackageName()));
        createContainer(FullProductView);
        findButtons(FullProductView);
        fillContainer();
        return FullProductView;
    }
    void createRecipe()
    {
        recipe=product.getRecipe();
        for(int i=0;i<recipe.size();i++)
        {
            TextView textView=new TextView(getContext());
            textView.setText("Шаг "+(i+1));
            textView.setTextSize(30);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.GRAY);
            ViewGroup.LayoutParams newStepParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            recipeLinear.addView(textView,newStepParams);
            TextView textView1=new TextView(getContext());
            textView1.setText(recipe.get(i));
            textView1.setTextSize(15);
            textView1.setGravity(Gravity.CENTER);
            textView1.setTextColor(Color.WHITE);
            recipeLinear.addView(textView1,newStepParams);
        }
    }
    void createContainer(View view)
    {
        name=(TextView)view.findViewById(R.id.fullProductName);
        time=(TextView)view.findViewById(R.id.fullproductTime);
        image=(ImageView)view.findViewById(R.id.fullProductImage);
        storage = FirebaseStorage.getInstance();
    }
    void fillContainer()
    {
        name.setText(product.getName());
        time.setText(product.getTime());
        createRecipe();
        makeIngs();
      //  recipe.setText(product.getRecipe());
        setImage();
    }
    void setImage()
    {
        storageReference=storage.getReferenceFromUrl("gs://samsungproject-357de.appspot.com").child(product.getImage());
        try {
            File localfile=File.createTempFile("images","jpg");
            storageReference.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                    image.setImageBitmap(bitmap);

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void makeIngs()
    {
        ings=product.getIngs();
        TextView textView=new TextView(getContext());
        textView.setText("Ингредиенты:");
        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.GRAY);
        ViewGroup.LayoutParams newStepParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ingsLinear.addView(textView,newStepParams);
        for(int i=0;i<ings.size();i++)
        {
            TextView textView1=new TextView(getContext());
            textView1.setText("-"+ings.get(i));
            textView1.setTextSize(15);
            textView1.setGravity(Gravity.CENTER);
            textView1.setTextColor(Color.WHITE);
            ingsLinear.addView(textView1,newStepParams);
        }
    }
    void findButtons(View view)
    {
        homeButton=(Button)view.findViewById(R.id.fullProductImageButton);
        homeButton.setOnClickListener(homeButtonClick);
        recipeLinear=view.findViewById(R.id.fullProductRecipe);
        ingsLinear=view.findViewById(R.id.fullProductIngs);
    }
    View.OnClickListener homeButtonClick=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getFragmentManager().beginTransaction().replace(R.id.main,new StartFragment()).commit();
        }
    };
}
