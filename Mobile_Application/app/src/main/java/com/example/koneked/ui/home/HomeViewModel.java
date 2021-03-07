package com.example.koneked.ui.home;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
    }

    public void setText(MutableLiveData<String> text) {
        mText = text;
    }

    public void setImage(ImageView img, int id, String contentDescription) {
        img.setImageResource(id);
        img.setContentDescription(contentDescription);
    }

    public void setButton(Button bt, boolean state) {
        bt.setEnabled(state);
    }

    public void setProgressBarVisibility(ProgressBar bar, int state) {
        bar.setVisibility(state);
    }

    public LiveData<String> getText() {
        return mText;
    }
}