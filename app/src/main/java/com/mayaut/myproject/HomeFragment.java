package com.mayaut.myproject;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        boolean alarmStarted;
//        if (!alarmStarted) {
//            alarmStarted = true;
//
//            new Handler().postDelayed(() -> {
//                NavController navController = Navigation.findNavController(view);
//                navController.navigate(R.id.alarmFragment);
//            }, 5000); // 5 seconds
//        }
    }
}
