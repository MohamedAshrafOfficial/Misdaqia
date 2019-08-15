package com.example.misdaqia.Controllers;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.misdaqia.Adapters.CategoriesAdapter;
import com.example.misdaqia.Model.Category;
import com.example.misdaqia.Services.ApiClient;
import com.example.misdaqia.View.CategoryDetailsActivity;
import com.example.misdaqia.VolleyUtils.MySingleton;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryDetailsController {
    private String TAG = "CategoryDetailsController";
    Context context;
    ArrayList<Category> categories = new ArrayList<>();
    Category category = new Category();
    CategoriesAdapter adapter;
    public CategoryDetailsController(Context context, CategoriesAdapter adapter, ArrayList<Category> categories){
        this.context = context;
        this.categories = categories;
        this.adapter = adapter;
    }


    public void getCategory(final String currentCategory){
        String url = ApiClient.BASE_URL +currentCategory;
          StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            for (int x = 0; x < 1; x++) {
                                JSONObject currentObject = jsonObject.getJSONObject(x+"");
                                category.setId(currentObject.getInt("id"));
                                category.setCreated_at(currentObject.getString("created_at"));
                                category.setUpdated_at(currentObject.getString("updated_at"));
                                category.setType(currentObject.getString("type"));
                                category.setYear(currentObject.getInt("year"));
                                category.setModel(currentObject.getString("model"));
                                category.setVendor(currentObject.getString("vendor"));
                                category.setColor(currentObject.getString("color"));
                                category.setNeww(currentObject.getInt("new"));
                                category.setOwnerID(currentObject.getInt("ownerID"));
                                category.setPrice(currentObject.getInt("price"));

                                categories.add(category);
                                category = new Category();
                            }
                            adapter.notifyDataSetChanged();
                        }catch (Exception ex){
                            Log.d(TAG, "onResponse:ERROR " + ex.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.getMessage());
                Log.d(TAG, "onErrorResponse: "+error);
                Log.d(TAG, "onErrorResponse: " + error.networkResponse);
                Log.d(TAG, "onErrorResponse: " + error.getLocalizedMessage());
                Log.d(TAG, "onErrorResponse: " + error.getNetworkTimeMs());

            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getmInsance(context).addToRequestQueue(stringRequest);

    }
}
