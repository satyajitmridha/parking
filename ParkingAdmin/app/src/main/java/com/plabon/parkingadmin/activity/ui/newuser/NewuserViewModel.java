package com.plabon.parkingadmin.activity.ui.newuser;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewuserViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NewuserViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}