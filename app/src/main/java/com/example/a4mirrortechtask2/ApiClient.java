package com.example.a4mirrortechtask2;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiClient {

    @GET("https://humorstech.com/humors_app/harh.php?id=1")
    Observable<MyData> getMyData();

}
