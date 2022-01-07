package com.example.a4mirrortechtask2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import app.futured.donut.DonutProgressView;
import app.futured.donut.DonutSection;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class MainActivity extends AppCompatActivity {

    private DonutProgressView donutProgressView;
    private List<DonutSection> donutSectionList = new ArrayList<>();
    private TextView maxValueTextView, currentValueTextView, percentageCompleteTextView;

    ApiClient apiClient;
    Disposable disposable;

    private RequestQueue requestQueue;

    private int maxValue = 5, currentValue = 1;
    private int percentageComplete;

    private ViewModel viewModel;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            init();
        } catch (JSONException e) {
            Log.e("TAG", "onCreate: Error in MainActivity");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (disposable.isDisposed()) {
            disposable = Observable.interval(0, 1000,
                    TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::callDataEndpoint, this::onError);
        }
    }

    private void onError(Throwable throwable) {
        Toast.makeText(this, "Error in Observable timer" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
        Log.e("TAG", "onError: " +  throwable.toString() );
    }

    @SuppressLint("CheckResult")
    private void callDataEndpoint(Long aLong) {

        Observable<MyData> observable = apiClient.getMyData();
        observable.subscribeOn(Schedulers.newThread()).
                observeOn(AndroidSchedulers.mainThread())
                .map(result -> result)
                .subscribe(this::handleResults, this::handleError);

    }

    private void handleError(Throwable throwable) {
        maxValueTextView.setText("-");
        currentValueTextView.setText("-");
        percentageCompleteTextView.setText("-");
    }

    @SuppressLint("SetTextI18n")
    private void handleResults(MyData myData) {
        if (myData != null) {
            Log.e("TAG", "" + myData.getCurrentValue());
            maxValue = myData.getMaxValue();
            currentValue = myData.getCurrentValue();
            Log.e("TAG", "" + maxValue + currentValue);
            percentageComplete = currentValue * 100 / maxValue;
            Log.e("TAG", "" + percentageComplete);
            maxValueTextView.setText(Integer.toString(maxValue));
            currentValueTextView.setText(Integer.toString(currentValue));
            percentageCompleteTextView.setText(percentageComplete + "%");
        } else {
            Toast.makeText(this, "No Result found", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        disposable.dispose();
    }

    private void init() throws JSONException {
        initView();
        fetchData();
//        setObservers();
        setDonutView();
//        getDataUsingVolley();
    }

    private void fetchData() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        disposable = Observable.interval(1000, 5000,
                TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::callDataEndpoint, this::onError);
    }

    private void initView() {
        maxValueTextView = findViewById(R.id.max_value);
        currentValueTextView = findViewById(R.id.current_value);
        donutProgressView = findViewById(R.id.donutView);
        percentageCompleteTextView = findViewById(R.id.percentage_complete);

        viewModel = new ViewModelProvider(this).get(ViewModel.class);

        requestQueue = Volley.newRequestQueue(this);

        apiClient = RetrofitInstance.getApiService();
    }

    @SuppressLint("SetTextI18n")
    public void setObservers() {
//        viewModel.getLiveData().observe(this, myData -> {
//            maxValue = myData.getMaxValue();
//            currentValue = myData.getCurrentValue();
//            percentageComplete = currentValue * 100 / maxValue;
//            maxValueTextView.setText("" + myData.getMaxValue());
//            currentValueTextView.setText("" + myData.getCurrentValue());
//            setDonutView();
//        });

    }

    @SuppressLint("SetTextI18n")
    private void setDonutView() {
        percentageCompleteTextView.setText(percentageComplete + "%");
        DonutSection d1 = new DonutSection("current_value", Color.parseColor("#ffbb33"), currentValue);
        donutProgressView.setCap(maxValue);
        donutSectionList.clear();
        donutSectionList.add(d1);
        donutProgressView.submitData(donutSectionList);

    }

    private void getDataUsingVolley() {
        String url = "https://humorstech.com/humors_app/harh.php?id=1";

        @SuppressLint("SetTextI18n") JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        maxValue = Integer.parseInt(response.getString("max_value"));
                        currentValue = Integer.parseInt(response.getString("value"));
                        percentageComplete = currentValue * 100 / maxValue;
                        Log.e("TAG", "Percentage complete is:" + percentageComplete);
                        Log.e("TAG", "Percentage complete is:" + currentValue);
                        Log.e("TAG", "Percentage complete is:" + maxValue);
                        maxValueTextView.setText(Integer.toString(maxValue));
                        currentValueTextView.setText(Integer.toString(currentValue));
                        setDonutView();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("TAG", "getDataUsingVolley: Error in getting data from volley")
        );
        requestQueue.add(getRequest);
    }
}