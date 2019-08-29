package com.example.misdaqia.View;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.misdaqia.Common.Common;
import com.example.misdaqia.Model.LoginUserResponse;
import com.example.misdaqia.R;
import com.example.misdaqia.Services.ApiClient;
import com.example.misdaqia.Services.JsonPlaceHolderApi;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    int timeSec;

    JsonPlaceHolderApi jsonPlaceHolderApi;
    private ProgressBar progressBar;

    PackageInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


//        try {
//            info = getPackageManager().getPackageInfo("com.example.misdaqia", PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md;
//                md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                String something = new String(Base64.encode(md.digest(), 0));
//                //String something = new String(Base64.encodeBytes(md.digest()));
//                Log.e("hash key", something);
//            }
//        } catch (PackageManager.NameNotFoundException e1) {
//            Log.e("name not found", e1.toString());
//        } catch (NoSuchAlgorithmException e) {
//            Log.e("no such an algorithm", e.toString());
//        } catch (Exception e) {
//            Log.e("exception", e.toString());
//        }




        jsonPlaceHolderApi = ApiClient.getApiClient().create(JsonPlaceHolderApi.class);


        Thread splash_screen = new Thread() {
            //for not moving to any activity after 3000
            @Override
            public void run() {
                // This method will be executed once the timer is over
                timeSec = 1500;
                // Start your app main activity
                try {
                    sleep(timeSec);

                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                } finally {

                    // it must return to main thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SharedPreferences share = getSharedPreferences("data", Context.MODE_PRIVATE);
                            String email = share.getString("email", null);
                            String password = share.getString("password", null);

                            if (!TextUtils.isEmpty(email)) {
                                Login(email, password);

                            } else {
                                Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }
        };

        splash_screen.start();


        initView();
    }


    private void Login(final String email, final String password) {


        if (Common.isConnectToInternet(getBaseContext())) {

//            final ProgressDialog progressDialog = new ProgressDialog(SplashActivity.this);
//            progressDialog.setMessage("please wait ....");
//            progressDialog.show();

            Call<LoginUserResponse> call = jsonPlaceHolderApi.loginUser(email, password);

            call.enqueue(new Callback<LoginUserResponse>() {
                @Override
                public void onResponse(Call<LoginUserResponse> call, Response<LoginUserResponse> response) {


                    LoginUserResponse loginUserResponse = response.body();

                    if (loginUserResponse.getEmail().equals(email)) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();

                    } else {
                        startActivity(new Intent(SplashActivity.this, SignInActivity.class));
                        finish();
                    }


                }


                @Override
                public void onFailure(Call<LoginUserResponse> call, Throwable t) {
//                    progressDialog.dismiss();
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(SplashActivity.this, "" + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });


        } else {
            Toast.makeText(SplashActivity.this, "لا يوجد اتصال بالانترنت ", Toast.LENGTH_SHORT).show();
        }

    }


    private void initView() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }
}
