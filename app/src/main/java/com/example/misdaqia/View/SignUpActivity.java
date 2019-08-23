package com.example.misdaqia.View;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.misdaqia.Helper.MainFontButton;
import com.example.misdaqia.Helper.MainFontEdittext;
import com.example.misdaqia.Model.registerUserResponse;
import com.example.misdaqia.Model.User;
import com.example.misdaqia.R;
import com.example.misdaqia.Services.ApiClient;
import com.example.misdaqia.Services.JsonPlaceHolderApi;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private CircleImageView profileProfileImage;
    private MainFontEdittext edtname;
    private MainFontEdittext edtemail;
    private MainFontEdittext edtpassword;
    private MainFontEdittext edtPasswordVerified;
    private MainFontButton signBtn;

    ProgressDialog progressDialog;

    JsonPlaceHolderApi jsonPlaceHolderApi;


    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initView();

        initActions();

        jsonPlaceHolderApi = ApiClient.getApiClient().create(JsonPlaceHolderApi.class);


    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    private void initActions() {

        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = edtname.getText().toString();
                String email = edtemail.getText().toString();
                String password = edtpassword.getText().toString();
                String re_password = edtPasswordVerified.getText().toString();

                if (checkValidation() == false) {
                    return;
                } else {
                    createUser(name, email, password, re_password);

                }

            }
        });
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    private void createUser(String name, String email, String password, String re_password) {

        progressDialog.show();

        Call<registerUserResponse> call = jsonPlaceHolderApi.createUser(name, email, password, re_password);

        call.enqueue(new Callback<registerUserResponse>() {
            @Override
            public void onResponse(Call<registerUserResponse> call, Response<registerUserResponse> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }


                if (response.code() == 500) {
                    Toast.makeText(SignUpActivity.this, "exist", Toast.LENGTH_SHORT).show();
                }

                registerUserResponse registerUserResponse = response.body();

                User users = registerUserResponse.getUserInfo();

                SharedPreferences.Editor edit = getSharedPreferences("data", Context.MODE_PRIVATE).edit();
                edit.putString("email", users.getEmail());
                edit.putString("password", users.getPassword());
                edit.commit();

                progressDialog.dismiss();

                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                finish();
            }


            @Override
            public void onFailure(Call<registerUserResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(SignUpActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(SignUpActivity.this, "خطأ فى اتصال بالانترنت !", Toast.LENGTH_SHORT).show();
            }
        });

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    public void backLogin(View view) {
        startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    private void initView() {
        profileProfileImage = (CircleImageView) findViewById(R.id.profile_profile_image);
        edtname = (MainFontEdittext) findViewById(R.id.edtname);
        edtemail = (MainFontEdittext) findViewById(R.id.edtemail);
        edtpassword = (MainFontEdittext) findViewById(R.id.edtpassword);
        edtPasswordVerified = (MainFontEdittext) findViewById(R.id.edtverifiedpassword);
        signBtn = (MainFontButton) findViewById(R.id.signBtn);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("انتظر ...");

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    private boolean checkValidation() {

        boolean flag = false;

        if (edtname.getText().toString().length() == 0) {
            edtname.setError("الاسم فارغ");
            edtname.requestFocus();
            flag = false;

        } else if (edtemail.getText().toString().length() == 0) {
            edtemail.setError("البريد الالكتروني فارغ");
            edtemail.requestFocus();
            flag = false;

        } else if (edtpassword.getText().toString().length() == 0) {
            edtpassword.setError("كلمه المرور فارغه");
            edtpassword.requestFocus();
            flag = false;

        } else if (edtPasswordVerified.getText().toString().length() == 0) {
            edtPasswordVerified.setError("كلمه المرور فارغه");
            edtPasswordVerified.requestFocus();
            flag = false;

        } else if (!edtPasswordVerified.getText().toString().equals(edtpassword.getText().toString())) {
            edtPasswordVerified.setError("كلمه المرور غير مطابقه");
            edtPasswordVerified.requestFocus();
            flag = false;

        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edtemail.getText().toString().trim()).matches()) {
            edtemail.setError("لبريد الالكتروني غير صحيح");
            edtemail.requestFocus();
            flag = false;
        } else {
            flag = true;
        }

        return flag;

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }


}

