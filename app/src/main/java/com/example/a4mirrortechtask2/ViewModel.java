package com.example.a4mirrortechtask2;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ViewModel extends AndroidViewModel {
    private DataRepo dataRepo;

    public ViewModel(@NonNull Application application) {
        super(application);
        dataRepo = new DataRepo(application);
    }

    public LiveData<MyData> getLiveData() {
        return dataRepo.getMyDataMutableLiveData();
    }
}
