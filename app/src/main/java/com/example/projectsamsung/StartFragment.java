package com.example.projectsamsung;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import static com.example.projectsamsung.R.*;

public class  StartFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View StartFragmentView = inflater.inflate(layout.start_fragment, container, false);
        init(StartFragmentView);
        return StartFragmentView;
    }

    public void init(View view) {
        Button startButton = view.findViewById(id.startButton);
        Button bookOfRecipesButton = view.findViewById(id.bookOfRecipesButton);
        Button exitButton = view.findViewById(id.exitButton);
        startButton.setOnClickListener(startButtonClick);
        bookOfRecipesButton.setOnClickListener(bookOfRecipesButtonClick);
        exitButton.setOnClickListener(exitButtonClick);
    }
    View.OnClickListener startButtonClick= view -> getFragmentManager().beginTransaction().replace(id.main,new ChooseFragment()).commit();
    View.OnClickListener bookOfRecipesButtonClick= view -> getFragmentManager().beginTransaction().replace(id.main,new ListOfVariantsFragment(true)).commit();
    View.OnClickListener exitButtonClick= view -> System.exit(0);
}
