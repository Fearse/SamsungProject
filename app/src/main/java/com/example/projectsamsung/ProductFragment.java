package com.example.projectsamsung;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class ProductFragment extends Fragment {
    TextView name;
    TextView time;
    ImageView image;

    Product product;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    ProductFragment(Product product)
    {
        this.product=product;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ProductView = inflater.inflate(R.layout.product_fragment, container, false);
        ProductView.setOnClickListener(ProductViewClick);
        createContainer(ProductView);
        fillContainer();
             //  linearLayout.setBackgroundResource(getResources().getIdentifier(img,"drawable",getContext().getPackageName()));

        return ProductView;
    }
    View.OnClickListener ProductViewClick=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getFragmentManager().beginTransaction().replace(R.id.main,new FullProductFragment(product)).commit();
        }
    };
    void createContainer(View view)
    {
        name=(TextView)view.findViewById(R.id.productName);
        time=(TextView)view.findViewById(R.id.productTime);
        image= (ImageView) view.findViewById(R.id.productImage);
        storage = FirebaseStorage.getInstance();
    }
    void fillContainer()
    {
        name.setText(product.getName());
        time.setText(product.getTime());
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
}
