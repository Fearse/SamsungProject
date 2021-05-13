package com.example.projectsamsung;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class MakeProductFragment extends Fragment {

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference dataBase;
    private String USER_KEY="Product";

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private EditText productName;
    private Button getImage;
    private EditText productTime;
    private EditText productRecipe;
    private EditText productIngredients;
    private Button findImage;
    private Button downloadImage;
    private ImageView image;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View makeProductView = inflater.inflate(R.layout.make_product_fragment, container, false);
        //  linearLayout.setBackgroundResource(getResources().getIdentifier(img,"drawable",getContext().getPackageName()));
        init(makeProductView);
        return makeProductView;
    }
    void init(View view)
    {
        //getImage=(Button)view.findViewById(R.id.getImage);
       // getImage.setOnClickListener(getImageClick);

        image=view.findViewById(R.id.image);
        productName=view.findViewById(R.id.newProductName);
        productTime=view.findViewById(R.id.newProductTime);
        productRecipe=view.findViewById(R.id.newProductRecipe);
        productIngredients=view.findViewById(R.id.newProductIngredients);
        findImage=view.findViewById(R.id.newProductImage);
        findImage.setOnClickListener(findImageClick);
        downloadImage=view.findViewById(R.id.downloadProduct);
        downloadImage.setOnClickListener(downloadImageClick);

        storage = FirebaseStorage.getInstance();
        dataBase= FirebaseDatabase.getInstance().getReference(USER_KEY);
    }
    void uploadTextToDatabase(){
       // String id=dataBase.getKey();
        String name=productName.getText().toString();
        String ingredients=productIngredients.getText().toString();
        String recipe=productRecipe.getText().toString();
        String time=productTime.getText().toString();
        Product product=new Product(name,name,time,recipe,ingredients);
        //добавить условие на проверку пустоты
        dataBase.push().setValue(product);
    }
    View.OnClickListener findImageClick=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           chooseImage();
        }
    };
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
                image.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    View.OnClickListener getImageClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          storageReference=storage.getReferenceFromUrl("gs://samsungproject-357de.appspot.com").child(productName.getText().toString());
            try {
                File localfile=File.createTempFile("images","jpg");
                storageReference.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                        image.setImageBitmap(bitmap);

                    }
                });
                /*image.setImageURI(Uri.fromFile(localfile));
                Bitmap bitmap= BitmapFactory.decodeFile(localfile.getAbsolutePath());
                image.setImageBitmap(bitmap);*/
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };
    View.OnClickListener downloadImageClick=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(filePath != null)
            {
                uploadTextToDatabase();
                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle("Uploading...");
                progressDialog.show();
                StorageReference ref = storage.getReference().child(productName.getText().toString());
                ref.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                        .getTotalByteCount());
                                progressDialog.setMessage("Uploaded "+(int)progress+"%");
                            }

                        });
            }
        }
    };
}
