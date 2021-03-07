package com.example.koneked.ui.home;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.koneked.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HomeFragment extends Fragment implements View.OnClickListener {

    // UI Implementation
    private HomeViewModel homeViewModel;
    private Button buttonConnect;
    private ImageView img;
    private TextView textView;
    private ProgressBar progressBar;

    // Bluetooth connection variables
    private static String deviceName = null;
    private static String deviceHardwareAddress;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // UI Initialization
        textView = root.findViewById(R.id.home_text);
        img = root.findViewById(R.id.home_image);
        progressBar = root.findViewById(R.id.progressBar);
        buttonConnect = root.findViewById(R.id.connect_button);

        // Initial setting
        homeViewModel.setText(new MutableLiveData<>(getString(R.string.not_connected)));
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        homeViewModel.setImage(img, R.drawable.baseline_device_unknown, getString(R.string.not_connected_image));
        progressBar.setVisibility(View.GONE);
        buttonConnect.setOnClickListener(this);

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    // Select Bluetooth Device
    @Override
    public void onClick(View v) {
        deviceName = getString(R.string.device_name);
        startActivity(new Intent(getActivity(), MainActivity.class));
        return;

//        // Bluetooth Setup
//        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        if (bluetoothAdapter == null) {
//            //Display a toast notifying the user that their device does not support Bluetooth
//            Toast.makeText(getActivity(),R.string.no_bluetooth,Toast.LENGTH_SHORT).show();
//        }
//        else {
//            // Get List of Paired Bluetooth Device
//            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
//            if (pairedDevices.size() > 0) {
//                // There are paired devices. Get the name and address of each paired device.
//                for (BluetoothDevice device : pairedDevices) {
//                    if (device.getName().equals(getString(R.string.device_name))) {
//                        deviceName = device.getName();
//                        deviceHardwareAddress = device.getAddress(); // MAC address
//                        return;
//                    }
//                }
//            }
//            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//            builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title).setCancelable(false)
//                    .setPositiveButton(R.string.dialog_positive, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                            Intent intentOpenBluetoothSettings = new Intent();
//                            intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
//                            startActivity(intentOpenBluetoothSettings);
//                        }
//                    })
//                    .setNegativeButton(R.string.dialog_negative, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    });
//            AlertDialog alert = builder.create();
//            alert.show();
//        }
    }

    public HomeFragment() {
        textView = getView().findViewById(R.id.home_text);
        img = getView().findViewById(R.id.home_image);
        progressBar = getView().findViewById(R.id.progressBar);
        buttonConnect = getView().findViewById(R.id.connect_button);
    }

    public void setImage(int id, String contentDescription) {
        homeViewModel.setImage(img, id, contentDescription);
    }

    public void setButton(boolean state) {
        homeViewModel.setButton(buttonConnect, state);
    }

    public void setProgressBarVisibility(int value) {
        homeViewModel.setProgressBarVisibility(progressBar, value);
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceHardwareAddress() {
        return deviceHardwareAddress;
    }
}