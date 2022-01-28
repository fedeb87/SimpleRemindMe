package com.federicoberon.simpleremindme.ui.about;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.federicoberon.simpleremindme.R;

public class AboutViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AboutViewModel(Application application) {
        mText = new MutableLiveData<>();
        mText.setValue(((Context) application).getResources().getString(R.string.about_fragment_hint));
    }

    public LiveData<String> getText() {
        return mText;
    }
}