package com.example.koneked.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DashboardViewModel() {
        mText = new MutableLiveData<>();
    }

    public void setText(MutableLiveData<String> text) {
        mText = text;
    }

    public LiveData<String> getText() {
        return mText;
    }
}