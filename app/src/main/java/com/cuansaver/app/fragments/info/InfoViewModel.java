package com.cuansaver.app.fragments.info;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InfoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public InfoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Made by Team CJC\n\n" +
                "Cornelius Tantius\n" +
                "Jonathan Kristanto\n" +
                "Chrismorgan Shintaro");
    }

    public LiveData<String> getText() {
        return mText;
    }
}