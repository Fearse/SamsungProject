    package com.example.projectsamsung;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

public class ProductFragment extends Fragment {
    private ImageView image;
    private LinearLayout linearLayout;
    private ListOfVariantsFragment listOfVariantsFragment;
    private Product product;
    private FirebaseStorage storage;


    ProductFragment(Product product)
    {
        this.product=product;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ProductView = inflater.inflate(R.layout.product_fragment, container, false);
        ProductView.setOnClickListener(ProductViewClick);
        init(ProductView);
        return ProductView;
    }
    View.OnClickListener ProductViewClick=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FullProductFragment fullProductFragment=new FullProductFragment(product);
            fullProductFragment.setCodeFromList(listOfVariantsFragment.getCode());
            assert getFragmentManager() != null;
            getFragmentManager().beginTransaction().replace(R.id.main,fullProductFragment).commit();
        }
    };
    public void init(View view) {
        TextView name = view.findViewById(R.id.productName);
        name.setText(product.getName());

        TextView time = view.findViewById(R.id.productTime);
        time.setText(product.getTimeString());

        image = view.findViewById(R.id.productImage);

        linearLayout = view.findViewById(R.id.productFragmentLinear);
        storage = FirebaseStorage.getInstance();
        setImage();
    }
    public String getName()
    {
        return product.getName();
    }
    public int getTime()
    {
        return product.getTimeInt();
    }
    public void setGone()
    {
        linearLayout.setVisibility(View.GONE);
    }
    public void setVisible()
    {
        linearLayout.setVisibility(View.VISIBLE);
    }

    public void setImage() {
        if(isOnline(getContext())) {
            StorageReference storageReference = storage.getReferenceFromUrl("gs://samsungproject-357de.appspot.com").child(product.getImage());
            try {
                File localfile = File.createTempFile("images", "jpg");
                storageReference.getFile(localfile).addOnSuccessListener(taskSnapshot -> {
                    Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                    savePicture(bitmap);
                    image.setImageBitmap(bitmap);

                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            image.setImageDrawable(Drawable.createFromPath("/storage/emulated/0/Android/data/com.example.projectsamsung/cache/" + product.image+".jpg"));
        }
    }
    private String savePicture(Bitmap bitmap)
    {
        OutputStream fOut = null;
        try {
            File file = new File(getContext().getExternalCacheDir(), product.image+".jpg");
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        }
        catch (Exception e)
        {
            return e.getMessage();
        }
        return "";
    }
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
    public void setListOfVariantsFragment(ListOfVariantsFragment listOfVariantsFragment) {
        this.listOfVariantsFragment=listOfVariantsFragment;
    }
    public Product getProduct() {
        return product;
    }
}
