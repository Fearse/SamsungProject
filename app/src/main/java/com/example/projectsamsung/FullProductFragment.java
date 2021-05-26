package com.example.projectsamsung;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class FullProductFragment extends Fragment {
    private String codeFromList;
    private LinearLayout ingsLinear;
    private LinearLayout recipeLinear;
    private ImageView image;
    private Product product;
    private FirebaseStorage storage;

    FullProductFragment(Product product)
    {
        this.product=product;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View FullProductView = inflater.inflate(R.layout.full_product_fragment, container, false);
        init(FullProductView);
        return FullProductView;
    }
    public void createRecipe() {
        ArrayList<String> recipe = product.getRecipe();
        for(int i = 0; i< recipe.size(); i++)
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
    public void init(View view) {
        TextView name = view.findViewById(R.id.fullProductName);
        name.setText(product.getName());

        TextView time = view.findViewById(R.id.fullproductTime);
        time.setText(product.getTimeString());

        image = view.findViewById(R.id.fullProductImage);
        storage = FirebaseStorage.getInstance();
        Button homeButton = view.findViewById(R.id.fullProductImageButton);
        homeButton.setOnClickListener(homeButtonClick);
        recipeLinear=view.findViewById(R.id.fullProductRecipe);
        ingsLinear=view.findViewById(R.id.fullProductIngs);

        createRecipe();
        makeIngs();
        setImage();
    }

    public void setImage() {
        if (isOnline(getContext())) {
            StorageReference storageReference = storage.getReferenceFromUrl("gs://samsungproject-357de.appspot.com").child(product.getImage());
            try {
                File localfile = File.createTempFile("images", "jpg");
                storageReference.getFile(localfile).addOnSuccessListener(taskSnapshot -> {
                    Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                    image.setImageBitmap(bitmap);

                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            image.setImageDrawable(Drawable.createFromPath("/storage/emulated/0/Android/data/com.example.projectsamsung/cache/" + product.image+".jpg"));
        }
    }
    public void makeIngs() {
        ArrayList<String> ings = product.getIngs();
        TextView textView=new TextView(getContext());
        textView.setText("Ингредиенты:");
        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.GRAY);
        ViewGroup.LayoutParams newStepParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ingsLinear.addView(textView,newStepParams);
        for(int i = 0; i< ings.size(); i++)
        {
            TextView textView1=new TextView(getContext());
            textView1.setText("-"+ ings.get(i));
            textView1.setTextSize(15);
            textView1.setGravity(Gravity.CENTER);
            textView1.setTextColor(Color.WHITE);
            ingsLinear.addView(textView1,newStepParams);
        }
    }
    public void setCodeFromList(String code)
    {
        codeFromList=code;
    }
    View.OnClickListener homeButtonClick=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            assert getFragmentManager() != null;
            getFragmentManager().beginTransaction().replace(R.id.main,new ListOfVariantsFragment(codeFromList)).commit();
        }
    };
    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }
}
