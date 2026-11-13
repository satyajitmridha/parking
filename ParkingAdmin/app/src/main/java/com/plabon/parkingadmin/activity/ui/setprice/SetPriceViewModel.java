package com.plabon.parkingadmin.activity.ui.setprice;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SetPriceViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SetPriceViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}