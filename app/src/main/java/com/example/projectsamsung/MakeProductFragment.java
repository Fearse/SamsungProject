package com.example.projectsamsung;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class MakeProductFragment extends Fragment {

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference dataBase;
    private String USER_KEY="Product";
    private Uri filePath;
    private Spinner spinner;
    private Spinner categories;
    private Button addStep;
    private ArrayList<String> ings;
    private String code;
    private Button homeButton;
    private LinearLayout makeRecipeLinearLayout;
    private final int PICK_IMAGE_REQUEST = 71;
    private EditText productName;
    private EditText productTime;
    private ConstraintLayout makeProduct;
    private Button goToChooseFragment;
    private EditText productStep;
    private Button findImage;
    private Button downloadImage;
    private ImageView image;
    public MakeProductFragment getInstance()
    {
        return this;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View makeProductView = inflater.inflate(R.layout.make_product_fragment, container, false);
        //  linearLayout.setBackgroundResource(getResources().getIdentifier(img,"drawable",getContext().getPackageName()));
        init(makeProductView);
        return makeProductView;
    }
    public void setCode(String code){this.code=code;};
    public ArrayList<String>  getRecipe()
    {
        ArrayList<String> recipe = new ArrayList<>();
        for (int i=0;i<makeRecipeLinearLayout.getChildCount();i++) {
            EditText editText=(EditText)makeRecipeLinearLayout.getChildAt(i);
            recipe.add(editText.getText().toString());
        }
        return recipe;
    }
    public void init(View view)
    {
        code="";
        //getImage=(Button)view.findViewById(R.id.getImage);
       // getImage.setOnClickListener(getImageClick);
        addStep=view.findViewById(R.id.addStep);
        addStep.setOnClickListener(addStepClick);
        makeRecipeLinearLayout=view.findViewById(R.id.makeRecipe);
        makeProduct=view.findViewById(R.id.makeProduct);
        image=view.findViewById(R.id.image);
        homeButton=view.findViewById(R.id.newProducthomeButton);
        homeButton.setOnClickListener(homeButtonClick);
        productName=view.findViewById(R.id.newProductName);
        productTime=view.findViewById(R.id.newProductTime);
        productStep=view.findViewById(R.id.newProductStep);
        findImage=view.findViewById(R.id.newProductImage);
        findImage.setOnClickListener(findImageClick);
        downloadImage=view.findViewById(R.id.downloadProduct);
        downloadImage.setOnClickListener(downloadImageClick);
        goToChooseFragment=view.findViewById(R.id.goToChooseIngredients);
        goToChooseFragment.setOnClickListener(goToChooseClick);
        storage = FirebaseStorage.getInstance();
        dataBase= FirebaseDatabase.getInstance().getReference(USER_KEY);
        spinner=view.findViewById(R.id.spinner);
        categories=view.findViewById(R.id.categories);
    }
    public boolean checkTime()
    {
        String time=productTime.getText().toString();
        for (int i=0;i<time.length();i++)
        {
            if(time.charAt(i)<'0'||time.charAt(i)>'9')
                return false;
        }
        return true;
    }
    public void setIngs(ArrayList<String> ings)
    {
        this.ings=ings;
    }
    public void setIngredientReady()
    {
        goToChooseFragment.setBackgroundColor(Color.parseColor("#1D6322"));
        goToChooseFragment.setText("Изменить выбор");

    }
    public String checkSpinnerForString()
    {
        String time;
        int temp= Integer.parseInt(productTime.getText().toString());
        if(spinner.getSelectedItemPosition()==0)
        {
            if(temp%100>4&&temp%100<21||temp%10>4||temp%10==0)
                time=""+temp+" минут";
            else
                if(temp%10==1)
                    time=""+temp+" минута";
                else
                    time=""+temp+" минуты";
        }
        else
        {
            if(temp%100>4&&temp%100<21||temp%10>4||temp%10==0)
                time=""+temp+" часов";
            else
            if(temp%10==1)
                time=""+temp+" час";
            else
                time=""+temp+" часа";
        }
        return time;
    }
    public int checkSpinnerForInt()
    {
        int time;
        if(spinner.getSelectedItemPosition()==0)
        {
          time= Integer.parseInt((productTime.getText().toString()));
        }
        else
            time= Integer.parseInt(productTime.getText().toString())*60;
        return time;
    }
    void uploadTextToDatabase(){
       // String id=dataBase.getKey();
        String name=productName.getText().toString();
        ArrayList<String>  recipe =getRecipe();
     //   String recipe=productStep.getText().toString();
        String time=checkSpinnerForString();
        Product product=new Product(name,name,time,recipe,code,categories.getSelectedItem().toString());
        product.setTime(checkSpinnerForInt());
        product.setIngs(ings);
        //добавить условие на проверку пустоты
        dataBase.push().setValue(product);
    }
    View.OnClickListener findImageClick=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           chooseImage();
        }
    };
    public void chooseImage() {
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
            if(checkTime()) {
                if (filePath != null) {
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
                                    Toast.makeText(getContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                            .getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                                }

                            });
                }
            }
            else
                Toast.makeText(getContext(),"Ошибка ввода",Toast.LENGTH_SHORT).show();
        }
    };
    View.OnClickListener goToChooseClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          //  makeProduct.setVisibility(View.GONE);
            getFragmentManager().beginTransaction().hide(getInstance()).commit();
            getFragmentManager().beginTransaction().add(R.id.main,new ChooseFragment(getInstance())).commit();
        }
    };
    View.OnClickListener addStepClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //  makeProduct.setVisibility(View.GONE);
            EditText newStep=new EditText(getContext());
            newStep.setHint("Шаг");
            newStep.setGravity(Gravity.CENTER);
            newStep.setHintTextColor(Color.rgb(33,181,229));
            ViewGroup.LayoutParams newStepParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            makeRecipeLinearLayout.addView(newStep,newStepParams);
        }
    };
    View.OnClickListener homeButtonClick=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            StartFragment startFragment=new StartFragment();
            getFragmentManager().beginTransaction().replace(R.id.main,startFragment).commit();
        }
    };
}
