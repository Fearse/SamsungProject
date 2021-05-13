package com.example.projectsamsung;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.net.URI;

import static com.example.projectsamsung.R.*;

public class StartFragment extends Fragment {
    private Button startButton;
    private Button bookOfRecipesButton;
    private Button settingsButton;
    private Button exitButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View StartFragmentView = inflater.inflate(layout.start_fragment, container, false);
        findButton(StartFragmentView);
      //  linearLayout.setBackgroundResource(getResources().getIdentifier(img,"drawable",getContext().getPackageName()));
        return StartFragmentView;
    }

    void findButton(View view) {
        startButton = (Button) view.findViewById(id.startButton);
        bookOfRecipesButton = (Button) view.findViewById(id.bookOfRecipesButton);
        settingsButton = (Button) view.findViewById(id.settingsButton);
        exitButton = (Button) view.findViewById(id.exitButton);

        startButton.setOnClickListener(startButtonClick);
        bookOfRecipesButton.setOnClickListener(bookOfRecipesButtonClick);
        settingsButton.setOnClickListener(settingsButtonClick);
        exitButton.setOnClickListener(exitButtonClick);
    }
    View.OnClickListener startButtonClick=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getFragmentManager().beginTransaction().replace(id.main,new ChooseFragment()).commit();
        }
    };
    View.OnClickListener bookOfRecipesButtonClick=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ListOfVariantsFragment listOfVariantsFragment=new ListOfVariantsFragment(true);
            getFragmentManager().beginTransaction().replace(id.main,listOfVariantsFragment).commit();
            Toast.makeText(getContext(),"book",Toast.LENGTH_SHORT).show();
        }
    };
    View.OnClickListener settingsButtonClick=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(),"settings",Toast.LENGTH_SHORT).show();
        }
    };
    View.OnClickListener exitButtonClick=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            System.exit(0);
        }
    };
}
