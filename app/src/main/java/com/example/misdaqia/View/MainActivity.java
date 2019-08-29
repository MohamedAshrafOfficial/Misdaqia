package com.example.misdaqia.View;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.misdaqia.Adapters.MainCategoriesAdapter;
import com.example.misdaqia.Model.MainCategory;
import com.example.misdaqia.Model.MainCategoryResponse;
import com.example.misdaqia.R;
import com.example.misdaqia.Services.ApiClient;
import com.example.misdaqia.Services.JsonPlaceHolderApi;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private RecyclerView categoryRecycler;
    private RecyclerView mazadatRecycler;
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    ProgressDialog progressDialog;

    JsonPlaceHolderApi jsonPlaceHolderApi;

    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        jsonPlaceHolderApi = ApiClient.getApiClient().create(JsonPlaceHolderApi.class);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        getCategories();

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////

    private void getCategories() {

        progressDialog.show();
        Call<MainCategoryResponse> call = jsonPlaceHolderApi.getCategories();


        call.enqueue(new Callback<MainCategoryResponse>() {
            @Override
            public void onResponse(Call<MainCategoryResponse> call, Response<MainCategoryResponse> response) {

                List<MainCategory> list = response.body().getMainCategory();


                progressDialog.dismiss();

                categoryRecycler.setAdapter(new MainCategoriesAdapter(MainActivity.this, list));
            }

            @Override
            public void onFailure(Call<MainCategoryResponse> call, Throwable t) {
                progressDialog.dismiss();

                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(MainActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("خروج")
                    .setMessage("هل انت متاكد من الخروج!")
                    .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent a = new Intent(Intent.ACTION_MAIN);
                            a.addCategory(Intent.CATEGORY_HOME);
                            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(a);
                        }

                    })
                    .setNegativeButton("لا", null)
                    .show();

        }

    }
/////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.seller_main, menu);
        return true;
    }
/////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button_sign, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
/////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            startActivity(new Intent(MainActivity.this, Profile.class));

        } else if (id == R.id.nav_mazadate) {

        } else if (id == R.id.nav_favorite) {

        } else if (id == R.id.nav_mazadfawre) {

        } else if (id == R.id.nav_mazadmaftoh) {

        } else if (id == R.id.nav_mazadmosar) {

        } else if (id == R.id.nav_elmahfza) {

        } else if (id == R.id.nav_logout) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("خروج")
                    .setMessage("هل انت متاكد من الخروج!")
                    .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            mGoogleSignInClient.signOut()
                                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(MainActivity.this, "تم تسجيل الخروج", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            startActivity(new Intent(MainActivity.this, SignInActivity.class));
                            finish();
                        }

                    })
                    .setNegativeButton("لا", null)
                    .show();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////

    private void initView() {
        toolbar = findViewById(R.id.toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("please wait ....");
        categoryRecycler = (RecyclerView) findViewById(R.id.category_recycler);
//        mazadatRecycler = (RecyclerView) findViewById(R.id.mazadat_recycler);

//        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);

        categoryRecycler.setLayoutManager(new GridLayoutManager(this, 2));
//        mazadatRecycler.setLayoutManager(new LinearLayoutManager(this));
    }
/////////////////////////////////////////////////////////////////////////////////////////////////


}
