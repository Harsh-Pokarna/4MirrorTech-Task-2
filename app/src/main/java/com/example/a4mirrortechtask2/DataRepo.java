package com.example.a4mirrortechtask2;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.Observable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataRepo {
    private MyData myData = new MyData();
    private MutableLiveData<MyData> myDataMutableLiveData = new MutableLiveData<>();
    private Application application;

    public DataRepo(Application application) {
        this.application = application;
    }

//    public MutableLiveData<MyData> getMyDataMutableLiveData() {
//        ApiClient apiClient = RetrofitInstance.getApiService();
//        Observable<MyData> call = apiClient.getMyData();
//        call.enqueue(new Callback<MyData>() {
//            @Override
//            public void onResponse(Call<MyData> call, Response<MyData> response) {
//                MyData md = response.body();
//                if (md != null) {
//                    myData = md;
//                    myDataMutableLiveData.setValue(myData);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MyData> call, Throwable t) {
//                Log.e("TAG", "Error thrown in DataRepo");
//
//            }
//        });
//        return myDataMutableLiveData;
//    }
}
