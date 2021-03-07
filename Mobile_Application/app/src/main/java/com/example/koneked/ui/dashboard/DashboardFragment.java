package com.example.koneked.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.koneked.R;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Initial setting
        final TextView device_text = root.findViewById(R.id.device_test);
        final TextView api_text = root.findViewById(R.id.api_test);
        final TextView motor1_text = root.findViewById(R.id.motor1_test);
        final TextView motor2_text = root.findViewById(R.id.motor2_test);
        final TextView motor3_text = root.findViewById(R.id.motor3_test);

        dashboardViewModel.setText(new MutableLiveData<>(getString(R.string.device_test)));
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                device_text.setText(s);
            }
        });

        dashboardViewModel.setText(new MutableLiveData<>(getString(R.string.API_test)));
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                api_text.setText(s);
            }
        });

        dashboardViewModel.setText(new MutableLiveData<>(getString(R.string.Motor_1_test)));
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                motor1_text.setText(s);
            }
        });

        dashboardViewModel.setText(new MutableLiveData<>(getString(R.string.Motor_2_test)));
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                motor2_text.setText(s);
            }
        });

        dashboardViewModel.setText(new MutableLiveData<>(getString(R.string.Motor_3_test)));
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                motor3_text.setText(s);
            }
        });

        return root;
    }
}