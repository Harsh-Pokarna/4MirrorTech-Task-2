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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.futured.donut.DonutProgressView;
import app.futured.donut.DonutSection;

public class MainActivity extends AppCompatActivity {

    private DonutProgressView donutProgressView;
    private List<DonutSection> donutSectionList = new ArrayList<>();
    private TextView maxValueTextView, currentValueTextView, percentageCompleteTextView;

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
            maxValueTextView.setText("There is an error");
        }
    }

    private void init() throws JSONException {
        initView();
//        setObservers();
        setDonutView();
        getDataUsingVolley();
    }

    private void initView() {
        maxValueTextView = findViewById(R.id.max_value);
        currentValueTextView = findViewById(R.id.current_value);
        donutProgressView = findViewById(R.id.donutView);
        percentageCompleteTextView = findViewById(R.id.percentage_complete);

        viewModel = new ViewModelProvider(this).get(ViewModel.class);

        requestQueue = Volley.newRequestQueue(this);
    }

    @SuppressLint("SetTextI18n")
    public void setObservers() {
        viewModel.getLiveData().observe(this, myData -> {
            maxValue = myData.getMaxValue();
            currentValue = myData.getCurrentValue();
            maxValueTextView.setText("The max value is: " + myData.getMaxValue());
            currentValueTextView.setText("The current value is:  " + myData.getCurrentValue());
            setDonutView();
        });

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