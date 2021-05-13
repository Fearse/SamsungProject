package com.example.projectsamsung;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

public class FullProductFragment extends Fragment {
    Button homeButton;
    TextView name;
    TextView time;
    LinearLayout linearLayout;
    ImageView image;
    TextView recipe;
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
        makeRecipe(FullProductView);
        findButtons(FullProductView);
        fillContainer();
        return FullProductView;
    }
    void createContainer(View view)
    {
        name=(TextView)view.findViewById(R.id.fullProductName);
        time=(TextView)view.findViewById(R.id.fullproductTime);
        recipe=view.findViewById(R.id.recipe);
        image=(ImageView)view.findViewById(R.id.fullProductImage);
        storage = FirebaseStorage.getInstance();
    }
    void makeRecipe(View view)
    {
       /* for (int i=0;i<product.getSteps().length;i++)
        {
            TextView text=new TextView(getContext());
            text.setText(product.getSteps()[i]);
            text.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.addView(text);
            //getFragmentManager().beginTransaction().add(R.id.recipeSteps,text).commit();
        }*/
    }
    void fillContainer()
    {
        name.setText(product.getName());
        time.setText(product.getTime());
        recipe.setText(product.getRecipe());
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
    void findButtons(View view)
    {
        homeButton=(Button)view.findViewById(R.id.fullProductImageButton);
        homeButton.setOnClickListener(homeButtonClick);
    }
    View.OnClickListener homeButtonClick=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getFragmentManager().beginTransaction().replace(R.id.main,new StartFragment()).commit();
        }
    };
}
