package com.example.a4mirrortechtask2;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiClient {

    @GET("https://humorstech.com/humors_app/harh.php?id=1")
    Call<MyData> getMyData();

}
