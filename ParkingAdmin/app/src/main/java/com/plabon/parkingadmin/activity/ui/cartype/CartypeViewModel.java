package com.plabon.parkingadmin.activity.ui.cartype;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CartypeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CartypeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}