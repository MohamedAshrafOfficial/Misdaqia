package com.example.misdaqia.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.misdaqia.Adapters.CategoriesAdapter;
import com.example.misdaqia.Common.Common;
import com.example.misdaqia.Controllers.CategoryDetailsController;
import com.example.misdaqia.Model.Category;
import com.example.misdaqia.Model.User;
import com.example.misdaqia.R;
import com.example.misdaqia.Services.ApiClient;
import com.example.misdaqia.Services.JsonPlaceHolderApi;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryDetailsActivity extends AppCompatActivity {

    RecyclerView categoryRecyclerView;
    CategoriesAdapter adapter;
    ArrayList<Category> categories = new ArrayList<>();
    JsonPlaceHolderApi jsonPlaceHolderApi;
    CategoryDetailsController categoryDetailsController;
    private static final String TAG = "CategoryDetailsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);

        initCategoriesRecycler();
        categoryDetailsController = new CategoryDetailsController(this, adapter, categories);
        categoryDetailsController.getCategory("getvichleapi");
    }

    private void initCategoriesRecycler(){
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CategoriesAdapter(categories,CategoryDetailsActivity.this);
        categoryRecyclerView.setAdapter(adapter);
    }
}