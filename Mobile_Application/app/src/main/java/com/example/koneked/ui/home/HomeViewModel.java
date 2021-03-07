package com.example.koneked.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.koneked.R;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
    }

    public void setText(MutableLiveData<String> text) {
        mText = text;
    }

    public LiveData<String> getText() {
        return mText;
    }
}