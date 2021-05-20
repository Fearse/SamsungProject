package com.example.projectsamsung;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import androidx.fragment.app.Fragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class MakeProductFragment extends Fragment {

    private FirebaseStorage storage;
    private DatabaseReference dataBase;
    private Uri filePath;
    private Spinner spinner;
    private String imageName;
    private Spinner categories;
    private ArrayList<String> ings;
    private String code;
    private LinearLayout makeRecipeLinearLayout;
    private final int PICK_IMAGE_REQUEST = 71;
    private EditText productName;
    private EditText productTime;
    private Button goToChooseFragment;
    private ImageView image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View makeProductView = inflater.inflate(R.layout.make_product_fragment, container, false);
        init(makeProductView);
        return makeProductView;
    }
    public void setCode(String code){this.code=code;}

    public ArrayList<String>  getRecipe() {
        ArrayList<String> recipe = new ArrayList<>();
        for (int i=0;i<makeRecipeLinearLayout.getChildCount();i++) {
            EditText editText=(EditText)makeRecipeLinearLayout.getChildAt(i);
            recipe.add(editText.getText().toString());
        }
        return recipe;
    }
    public void init(View view) {
        String KEY="Product";
        Button addStep = view.findViewById(R.id.addStep);
        addStep.setOnClickListener(addStepClick);
        makeRecipeLinearLayout=view.findViewById(R.id.makeRecipe);
        image=view.findViewById(R.id.image);
        Button homeButton = view.findViewById(R.id.newProducthomeButton);
        homeButton.setOnClickListener(homeButtonClick);
        productName=view.findViewById(R.id.newProductName);
        productTime=view.findViewById(R.id.newProductTime);
        Button findImage = view.findViewById(R.id.newProductImage);
        findImage.setOnClickListener(findImageClick);
        Button downloadImage = view.findViewById(R.id.downloadProduct);
        downloadImage.setOnClickListener(uploadImageClick);
        goToChooseFragment=view.findViewById(R.id.goToChooseIngredients);
        goToChooseFragment.setOnClickListener(goToChooseClick);
        storage = FirebaseStorage.getInstance();
        dataBase= FirebaseDatabase.getInstance().getReference(KEY);
        spinner=view.findViewById(R.id.spinner);
        categories=view.findViewById(R.id.categories);
    }
    public boolean checkTime() {
        String time=productTime.getText().toString();
        for (int i=0;i<time.length();i++)
        {
            if(time.charAt(i)<'0'||time.charAt(i)>'9')
                return false;
        }
        return true;
    }
    public void setIngs(ArrayList<String> ings) {
        this.ings=ings;
    }
    public void setIngredientReady() {
        goToChooseFragment.setBackgroundColor(Color.parseColor("#1D6322"));
        goToChooseFragment.setText("Изменить выбор");
    }
    public String checkSpinnerForString() {
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
    public int checkSpinnerForInt() {
        int time;
        if(spinner.getSelectedItemPosition()==0)
        {
          time= Integer.parseInt((productTime.getText().toString()));
        }
        else
            time= Integer.parseInt(productTime.getText().toString())*60;
        return time;
    }
    public MakeProductFragment getInstance() {
        return this;
    }
    void uploadTextToDatabase(){
        String name=productName.getText().toString();
        ArrayList<String> recipe =getRecipe();
        String time=checkSpinnerForString();
        imageName=UUID.randomUUID().toString();
        Product product=new Product(name, imageName,time,recipe,code,categories.getSelectedItem().toString());
        product.setTime(checkSpinnerForInt());
        product.setIngs(ings);
        dataBase.push().setValue(product);
    }
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

    View.OnClickListener findImageClick= view -> chooseImage();
    View.OnClickListener uploadImageClick=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            EditText firstEd=(EditText)makeRecipeLinearLayout.getChildAt(0);
            if(checkTime()||productName.getText().toString().equals("")||firstEd.getText().toString().equals("")){
                if (filePath != null) {
                    uploadTextToDatabase();
                    final ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setTitle("Uploading");
                    progressDialog.show();
                    StorageReference ref = storage.getReference().child(imageName);
                    ref.putFile(filePath)
                            .addOnSuccessListener(taskSnapshot -> {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            })
                            .addOnProgressListener(taskSnapshot -> {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                        .getTotalByteCount());
                                progressDialog.setMessage("Uploaded " + (int) progress + "%");
                            });
                }
            }
            else
                Toast.makeText(getContext(),"Ошибка ввода",Toast.LENGTH_SHORT).show();
        }
    };
    View.OnClickListener goToChooseClick= v -> {
        assert getFragmentManager() != null;
        getFragmentManager().beginTransaction().hide(getInstance()).commit();
        getFragmentManager().beginTransaction().add(R.id.main,new ChooseFragment(getInstance())).commit();
    };
    View.OnClickListener addStepClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText newStep=new EditText(getContext());
            newStep.setHint("Шаг");
            newStep.setGravity(Gravity.CENTER);
            newStep.setHintTextColor(Color.rgb(33,181,229));
            ViewGroup.LayoutParams newStepParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            makeRecipeLinearLayout.addView(newStep,newStepParams);
        }
    };
    View.OnClickListener homeButtonClick= view -> {
        assert getFragmentManager() != null;
        getFragmentManager().beginTransaction().replace(R.id.main,new ListOfVariantsFragment(true)).commit();
    };
}
