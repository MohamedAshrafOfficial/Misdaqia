package com.example.misdaqia.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.misdaqia.Common.Common;
import com.example.misdaqia.Helper.MainFontButton;
import com.example.misdaqia.Helper.MainFontEdittext;
import com.example.misdaqia.Model.User;
import com.example.misdaqia.R;
import com.example.misdaqia.Services.ApiClient;
import com.example.misdaqia.Services.JsonPlaceHolderApi;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignUpActivity extends AppCompatActivity {

    private CircleImageView profileProfileImage;
    private MainFontEdittext edtname;
    private MainFontEdittext edtusername;
    private MainFontEdittext edtemail;
    private MainFontEdittext edtpassword;
    private MainFontEdittext edtPasswordVerified;
    private MainFontButton signBtn;

    ProgressDialog progressDialog;

    JsonPlaceHolderApi jsonPlaceHolderApi;

    User user;

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
                String user_name = edtusername.getText().toString();
                String email = edtemail.getText().toString();
                String password = edtpassword.getText().toString();

                if (checkValidation() == false) {
                    return;
                } else {
                    createUser(name, user_name, email, password, email);

                }

            }
        });
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    private void createUser(String name, String username, String email, String password, String verify) {

        progressDialog.show();

        Call<User> call = jsonPlaceHolderApi.CreateUser(name, username, email, password, verify);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                    if (response.code() == 500) {
                        Toast.makeText(SignUpActivity.this, "exist", Toast.LENGTH_SHORT).show();
                    }
                    return;

                } else {

                    User user = response.body();

                    Common.currentUser.setUsername(user.getName());

                    Toast.makeText(SignUpActivity.this, "" +  Common.currentUser.getName(), Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();

//                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
//                    finish();
                }


            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "خطأ فى اتصال بالانترنت !", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
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
        edtusername = (MainFontEdittext) findViewById(R.id.edtusername);
        edtemail = (MainFontEdittext) findViewById(R.id.edtemail);
        edtpassword = (MainFontEdittext) findViewById(R.id.edtpassword);
        edtPasswordVerified = (MainFontEdittext) findViewById(R.id.edtverifiedpassword);
        signBtn = (MainFontButton) findViewById(R.id.signBtn);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("waiting...");

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    private boolean checkValidation() {

        boolean flag = false;

        if (edtname.getText().toString().length() == 0) {
            edtname.setError("الاسم فارغ");
            edtname.requestFocus();
            flag = false;

        } else if (edtusername.getText().toString().length() == 0) {
            edtusername.setError("اسم المستخدم فارغ");
            edtusername.requestFocus();
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

